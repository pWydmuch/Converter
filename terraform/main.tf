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

module "jumpbox" {
  count = var.enable_jumpbox ? 1 : 0
  source = "./jumpbox"
  vpc_settings = {
    security_group_id = aws_security_group.lambda_sg.id
    subnet_id         = aws_subnet.private.id
    vpc_id            = aws_vpc.main.id
  }
}

variable "enable_cloudfront" {
  type    = bool
  default = false
}

variable "enable_jumpbox" {
  type    = bool
  default = false
}

output "cloudfront_domain" {
  value = var.enable_cloudfront ? module.cloudfront[0].cloudfront_domain : null
}

output "instance_id" {
  value = var.enable_jumpbox ? module.jumpbox[0].instance_id : null
}

output "public_ip" {
  value = var.enable_jumpbox ? module.jumpbox[0].public_ip : null
}
