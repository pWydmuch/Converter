resource "aws_s3_bucket" "frontend_config" {
  bucket = "converter-frontend-config"
}

resource "aws_s3_bucket_versioning" "frontend_config" {
  bucket = aws_s3_bucket.frontend_config.id
  versioning_configuration {
    status = "Enabled"
  }
}

resource "aws_s3_bucket_public_access_block" "frontend_config" {
  bucket = aws_s3_bucket.frontend_config.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_s3_object" "api_url" {
  bucket = aws_s3_bucket.frontend_config.id
  key    = "api-url.txt"
  content = aws_apigatewayv2_stage.converter_stage.invoke_url
  content_type = "text/plain"
  acl    = "public-read"
} 