module "cloudfront" {
  count = var.enable_cloudfront ? 1 : 0
  source = "./cloudfront"
  frontend_bucket = {
    id                          = aws_s3_bucket.frontend.id
    arn                         = aws_s3_bucket.frontend.arn
    bucket                      = aws_s3_bucket.frontend.bucket
    bucket_domain_name          = aws_s3_bucket.frontend.bucket_domain_name
    bucket_regional_domain_name = aws_s3_bucket.frontend.bucket_regional_domain_name
  }
}

variable "enable_cloudfront" {
  type    = bool
  default = false
}

output "cloudfront_domain" {
  value = var.enable_cloudfront ? module.cloudfront[0].cloudfront_domain : null
}
