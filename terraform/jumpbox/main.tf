resource "aws_instance" "my_instance" {
  ami                    = "ami-03d8b47244d950bbb"
  instance_type          = "t2.micro"
  key_name               = "aws-training"
  vpc_security_group_ids = [var.vpc_settings.security_group_id]
  iam_instance_profile   = aws_iam_instance_profile.jumpbox_profile.name
  subnet_id              = var.vpc_settings.subnet_id
  tags                   = {
    Name = "jumpbox"
  }
}

resource "aws_iam_role" "jumpbox_ssm_role" {
  name               = "JumpboxSSMRole"
  assume_role_policy = jsonencode({
    Version   = "2012-10-17"
    Statement = [
      {
        Effect    = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "jumpbox_ssm_core" {
  role       = aws_iam_role.jumpbox_ssm_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_instance_profile" "jumpbox_profile" {
  name = "JumpboxSSMProfile"
  role = aws_iam_role.jumpbox_ssm_role.name
}

# TODO conditionally if jumpbox, you got billed for these endpoints
resource "aws_vpc_endpoint" "ssm" {
  vpc_id             = var.vpc_settings.vpc_id
  service_name       = "com.amazonaws.eu-west-1.ssm"
  subnet_ids         = [var.vpc_settings.subnet_id]
  security_group_ids = [var.vpc_settings.security_group_id]
  vpc_endpoint_type  = "Interface"
  private_dns_enabled = true
}
resource "aws_vpc_endpoint" "ssmmessages" {
  vpc_id             = var.vpc_settings.vpc_id
  service_name       = "com.amazonaws.eu-west-1.ssmmessages"
  subnet_ids         = [var.vpc_settings.subnet_id]
  security_group_ids = [var.vpc_settings.security_group_id]
  vpc_endpoint_type  = "Interface"
  private_dns_enabled = true
}
resource "aws_vpc_endpoint" "ec2messages" {
  vpc_id             = var.vpc_settings.vpc_id
  service_name       = "com.amazonaws.eu-west-1.ec2messages"
  subnet_ids         = [var.vpc_settings.subnet_id]
  security_group_ids = [var.vpc_settings.security_group_id]
  vpc_endpoint_type  = "Interface"
  private_dns_enabled = true
}

output "public_ip" {
  value = aws_instance.my_instance.public_ip
}

output "instance_id" {
  value = aws_instance.my_instance.id
}
