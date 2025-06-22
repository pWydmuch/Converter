# S3 bucket for frontend,
# TODO it's a bucket now you have this done without terraform
resource "aws_s3_bucket" "frontend" {
  bucket = "p11h-converter"
}
# Block all public access to the S3 bucket
resource "aws_s3_bucket_public_access_block" "frontend" {
  bucket = aws_s3_bucket.frontend.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}
