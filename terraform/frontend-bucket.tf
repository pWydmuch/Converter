# S3 bucket for frontend,
resource "aws_s3_bucket" "frontend" {
  bucket = "p11h-converter"
  force_destroy = true
}
# Block all public access to the S3 bucket
resource "aws_s3_bucket_public_access_block" "frontend" {
  bucket = aws_s3_bucket.frontend.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}
