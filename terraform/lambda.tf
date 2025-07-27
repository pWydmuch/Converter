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

resource "aws_iam_role_policy" "lambda_vpc_access" {
  name = "lambda-vpc-access"
  role = aws_iam_role.lambda_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ec2:CreateNetworkInterface",
          "ec2:DescribeNetworkInterfaces",
          "ec2:DeleteNetworkInterface"
        ]
        Resource = "*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "lambda_basic_execution" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
  role       = aws_iam_role.lambda_role.name
}

resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
  # TODO conditionally on jumpbox
  enable_dns_support   = var.enable_jumpbox
  enable_dns_hostnames = var.enable_jumpbox
}

resource "aws_subnet" "private" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = false
}

resource "aws_security_group" "lambda_sg" {
  name        = "lambda-sg"
  description = "Security group for Lambda functions"
  vpc_id      = aws_vpc.main.id

  # Outbound: allow all (default in AWS, but explicit here)
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # No ingress needed for Lambda (unless you invoke it from VPC, which is rare)
}
#TODO Is this security group rule necessary? -> yes it is
resource "aws_security_group_rule" "allow_https_from_self" {
  type                     = "ingress"
  from_port                = 443
  to_port                  = 443
  protocol                 = "tcp"
  security_group_id        = aws_security_group.lambda_sg.id
  source_security_group_id = aws_security_group.lambda_sg.id
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

  vpc_config {
    subnet_ids         = [aws_subnet.private.id]
    security_group_ids = [aws_security_group.lambda_sg.id]
  }

  environment {
    variables = {
      REDIS_HOST    = aws_elasticache_cluster.redis.cache_nodes[0].address
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

  vpc_config {
    subnet_ids         = [aws_subnet.private.id]
    security_group_ids = [aws_security_group.lambda_sg.id]
  }

  environment {
    variables = {
      REDIS_HOST    = aws_elasticache_cluster.redis.cache_nodes[0].address
    }
  }
}

resource "aws_lambda_function" "get_recents" {
  function_name = "get-recents"
  role          = aws_iam_role.lambda_role.arn
  s3_bucket     = aws_s3_bucket.lambda_bucket.id
  s3_key        = aws_s3_object.lambda.key
  runtime       = "java21"
  handler       = "com.example.converter.GetRecentsLambdaHandler::handleRequest"
  timeout       = 30
  memory_size   = 256

  vpc_config {
    subnet_ids         = [aws_subnet.private.id]
    security_group_ids = [aws_security_group.lambda_sg.id]
  }

  environment {
    variables = {
      REDIS_HOST    = aws_elasticache_cluster.redis.cache_nodes[0].address
    }
  }
}
