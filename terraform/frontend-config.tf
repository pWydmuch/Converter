resource "aws_s3_bucket" "frontend_config" {
  bucket = "p11h-converter-frontend-config"
}

resource "aws_s3_bucket_versioning" "frontend_config" {
  bucket = aws_s3_bucket.frontend_config.id
  versioning_configuration {
    status = "Enabled"
  }
}

resource "aws_s3_bucket_public_access_block" "frontend_config" {
  bucket = aws_s3_bucket.frontend_config.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

resource "aws_s3_object" "api_url" {
  bucket = aws_s3_bucket.frontend_config.id
  key    = "api-url.txt"
  content = aws_apigatewayv2_stage.converter_stage.invoke_url
  content_type = "text/plain"
} 