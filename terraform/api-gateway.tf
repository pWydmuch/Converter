resource "aws_apigatewayv2_api" "converter_api" {
  name          = "converter-api"
  protocol_type = "HTTP"
  cors_configuration {
    allow_methods = ["GET"]
    allow_origins = ["*"]
  }
}

resource "aws_apigatewayv2_stage" "converter_stage" {
  api_id      = aws_apigatewayv2_api.converter_api.id
  name        = "prod"
  auto_deploy = true
}

resource "aws_apigatewayv2_integration" "roman_to_arabic" {
  api_id           = aws_apigatewayv2_api.converter_api.id
  integration_type = "AWS_PROXY"

  description        = "Roman to Arabic conversion"
  integration_method = "POST"
  integration_uri    = aws_lambda_function.converter_to_arabic.invoke_arn
}

resource "aws_apigatewayv2_integration" "arabic_to_roman" {
  api_id           = aws_apigatewayv2_api.converter_api.id
  integration_type = "AWS_PROXY"

  description        = "Arabic to Roman conversion"
  integration_method = "POST"
  integration_uri    = aws_lambda_function.converter_to_roman.invoke_arn
}

resource "aws_apigatewayv2_integration" "get_recents" {
  api_id           = aws_apigatewayv2_api.converter_api.id
  integration_type = "AWS_PROXY"

  description        = "Get recent conversions"
  integration_method = "POST"
  integration_uri    = aws_lambda_function.get_recents.invoke_arn
}

resource "aws_apigatewayv2_route" "roman_to_arabic" {
  api_id    = aws_apigatewayv2_api.converter_api.id
  route_key = "GET /roman-to-arabic"
  target    = "integrations/${aws_apigatewayv2_integration.roman_to_arabic.id}"
}

resource "aws_apigatewayv2_route" "arabic_to_roman" {
  api_id    = aws_apigatewayv2_api.converter_api.id
  route_key = "GET /arabic-to-roman"
  target    = "integrations/${aws_apigatewayv2_integration.arabic_to_roman.id}"
}

resource "aws_apigatewayv2_route" "get_recents" {
  api_id    = aws_apigatewayv2_api.converter_api.id
  route_key = "GET /recents"
  target    = "integrations/${aws_apigatewayv2_integration.get_recents.id}"
}

resource "aws_lambda_permission" "roman_to_arabic" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.converter_to_arabic.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_apigatewayv2_api.converter_api.execution_arn}/*/*"
}

resource "aws_lambda_permission" "arabic_to_roman" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.converter_to_roman.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_apigatewayv2_api.converter_api.execution_arn}/*/*"
}

resource "aws_lambda_permission" "get_recents" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.get_recents.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_apigatewayv2_api.converter_api.execution_arn}/*/*"
}

output "api_endpoint" {
  value = aws_apigatewayv2_stage.converter_stage.invoke_url
} 