resource "random_pet" "lambda_bucket_name" {
  prefix = "p11h"
  length = 2
}

resource "aws_s3_bucket" "lambda_bucket" {
  bucket = random_pet.lambda_bucket_name.id
}

#TODO Get rid of it
resource "aws_s3_object" "lambda" {
  bucket = aws_s3_bucket.lambda_bucket.id
  key    = "converter.jar"
  source = "../target/converter-0.0.1-SNAPSHOT.jar"
}

resource "aws_s3_bucket_public_access_block" "lambda_bucket_public_access_block" {
  bucket = aws_s3_bucket.lambda_bucket.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}