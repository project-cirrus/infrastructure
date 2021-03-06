package org.cirrus.infrastructure.factory;

import java.util.List;
import org.cirrus.infrastructure.util.Keys;
import org.cirrus.infrastructure.util.Outputs;
import org.immutables.builder.Builder;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.services.apigatewayv2.AddRoutesOptions;
import software.amazon.awscdk.services.apigatewayv2.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.HttpMethod;
import software.amazon.awscdk.services.apigatewayv2.HttpStageOptions;
import software.amazon.awscdk.services.apigatewayv2.IHttpApi;
import software.amazon.awscdk.services.apigatewayv2.IHttpRouteAuthorizer;
import software.amazon.awscdk.services.apigatewayv2.authorizers.HttpUserPoolAuthorizer;
import software.amazon.awscdk.services.apigatewayv2.integrations.HttpLambdaIntegration;
import software.amazon.awscdk.services.cognito.IUserPool;
import software.amazon.awscdk.services.cognito.IUserPoolClient;
import software.amazon.awscdk.services.dynamodb.ITable;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.s3.IBucket;

final class NodeApiFactory {

  private static final String API_ID = "NodeApi";
  private static final String AUTHORIZER_ID = API_ID + "Authorizer";
  private static final String NODE_ENDPOINT = "/node";
  private static final String CODE_ENDPOINT = "/code";
  private static final String DEV_STAGE_ID = "DevStage";
  private static final String DEV_STAGE = "dev";
  private static final String API_URL_OUTPUT = "apiUrl";
  private static final String IDENTITY_SOURCE = "$request.header.Authorization";

  private NodeApiFactory() {
    // no-op
  }

  @Builder.Factory
  public static IHttpApi nodeApi(
      @Builder.Parameter Construct scope,
      ITable nodeTable,
      IBucket runtimeBucket,
      IBucket uploadBucket,
      IRole nodeRole) {
    HttpApi api = newApi(scope);
    addRoutes(scope, api, nodeTable, runtimeBucket, uploadBucket, nodeRole);
    addStages(api);
    addMetrics(api);
    return api;
  }

  private static HttpApi newApi(Construct scope) {
    HttpApi api =
        HttpApi.Builder.create(scope, API_ID).defaultAuthorizer(authorizer(scope)).build();
    return Outputs.output(scope, API_URL_OUTPUT, api, HttpApi::getUrl);
  }

  private static void addRoutes(
      Construct scope,
      HttpApi api,
      ITable nodeTable,
      IBucket runtimeBucket,
      IBucket uploadBucket,
      IRole nodeRole) {
    api.addRoutes(uploadCode(scope, uploadBucket));
    api.addRoutes(publishCode(scope));
    api.addRoutes(createNode(scope, nodeTable, runtimeBucket, uploadBucket, nodeRole));
    api.addRoutes(deleteNode(scope, nodeTable));
  }

  private static AddRoutesOptions uploadCode(Construct scope, IBucket uploadBucket) {
    IFunction handler = ApiHandlerFactory.uploadCodeHandler(scope, uploadBucket.getBucketName());
    uploadBucket.grantWrite(handler);
    return addCodeRouteOptions(handler, Keys.UPLOAD_HANDLER_NAME, List.of(HttpMethod.GET));
  }

  private static AddRoutesOptions publishCode(Construct scope) {
    IFunction handler = ApiHandlerFactory.publishCodeHandler(scope);
    handler.addToRolePolicy(IamFactory.publishCodePolicy());
    return addCodeRouteOptions(handler, Keys.PUBLISH_HANDLER_NAME, List.of(HttpMethod.POST));
  }

  private static AddRoutesOptions createNode(
      Construct scope,
      ITable nodeTable,
      IBucket runtimeBucket,
      IBucket uploadBucket,
      IRole nodeRole) {
    IFunction handler =
        ApiHandlerFactory.createNodeHandler(
            scope, nodeRole.getRoleArn(), runtimeBucket.getBucketName());
    nodeTable.grantWriteData(handler);
    uploadBucket.grantRead(handler);
    handler.addToRolePolicy(IamFactory.createNodePolicy());
    return addNodeRouteOptions(handler, Keys.CREATE_HANDLER_NAME, List.of(HttpMethod.POST));
  }

  private static AddRoutesOptions deleteNode(Construct scope, ITable nodeTable) {
    IFunction handler = ApiHandlerFactory.deleteNodeHandler(scope);
    nodeTable.grantWriteData(handler);
    handler.addToRolePolicy(IamFactory.deleteNodePolicy());
    return addNodeRouteOptions(handler, Keys.DELETE_HANDLER_NAME, List.of(HttpMethod.DELETE));
  }

  private static AddRoutesOptions addCodeRouteOptions(
      IFunction handler, String handlerName, List<HttpMethod> methods) {
    return addRouteOptions(handler, handlerName, CODE_ENDPOINT, methods);
  }

  private static AddRoutesOptions addNodeRouteOptions(
      IFunction handler, String handlerName, List<HttpMethod> methods) {
    return addRouteOptions(handler, handlerName, NODE_ENDPOINT, methods);
  }

  private static AddRoutesOptions addRouteOptions(
      IFunction handler, String handlerName, String endpoint, List<HttpMethod> methods) {
    return AddRoutesOptions.builder()
        .path(endpoint)
        .methods(methods)
        .integration(new HttpLambdaIntegration(handlerName, handler))
        .build();
  }

  private static void addStages(HttpApi api) {
    api.addStage(DEV_STAGE_ID, HttpStageOptions.builder().stageName(DEV_STAGE).build());
  }

  private static IHttpRouteAuthorizer authorizer(Construct scope) {
    IUserPool userPool = CognitoFactory.userPool(scope);
    IUserPoolClient client = CognitoFactory.userPoolClient(scope, userPool);
    CognitoFactory.userPoolDomain(scope, userPool);
    return HttpUserPoolAuthorizer.Builder.create(AUTHORIZER_ID, userPool)
        .userPoolClients(List.of(client))
        .identitySource(List.of(IDENTITY_SOURCE))
        .build();
  }

  // TODO This doesn't actually track the metrics
  private static void addMetrics(HttpApi api) {
    api.metricCount();
    api.metricClientError();
    api.metricLatency();
    api.metricServerError();
    api.metricDataProcessed();
  }
}
