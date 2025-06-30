resource "aws_iam_role" "lambda_role" {
  name               = "lambda_converter_role"
  assume_role_policy = jsonencode({
    Version   = "2012-10-17"
    Statement = [
      {
        Effect    = "Allow"
        Principal = {
          Service = "lambda.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "lambda_basic_execution" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
  role       = aws_iam_role.lambda_role.name
}

resource "aws_lambda_function" "converter_to_arabic" {
  function_name = "converter-to-arabic"
  role          = aws_iam_role.lambda_role.arn
  s3_bucket     = aws_s3_bucket.lambda_bucket.id
  s3_key        = aws_s3_object.lambda.key
  runtime       = "java21"
  handler       = "com.example.converter.ToArabicLambdaHandler::handleRequest"
  timeout       = 30
  memory_size   = 256

  environment {
    variables = {
      TEST_ENV_VAR = "test_value"
    }
  }
}

resource "aws_lambda_function" "converter_to_roman" {
  function_name = "converter-to-roman"
  role          = aws_iam_role.lambda_role.arn
  s3_bucket     = aws_s3_bucket.lambda_bucket.id
  s3_key        = aws_s3_object.lambda.key
  runtime       = "java21"
  handler       = "com.example.converter.ToRomanLambdaHandler::handleRequest"
  timeout       = 30
  memory_size   = 256
}