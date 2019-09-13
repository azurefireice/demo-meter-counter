locals {
  callback_lambda_filepath = "../lambda-java-counter-callback-${var.app_version}.jar"
  info_lambda_filepath     = "../lambda-java-counter-info-${var.app_version}.jar"
  report_lambda_filepath   = "../lambda-java-counter-report-${var.app_version}.jar"
}

variable "app_version" {
  type    = "string"
  default = "1.0-SNAPSHOT"
}

variable "region" {
  type    = "string"
  default = "us-east-2"
}

resource "aws_lambda_function" "counter_callback" {
  filename      = local.callback_lambda_filepath
  function_name = "meter-counter-callback-processor"

  handler     = "edu.demo.meter.counter.callback.CounterCallbackHandler"
  runtime     = "java8"
  memory_size = "320"
  timeout     = 16

  source_code_hash = filebase64sha256(local.callback_lambda_filepath)
  role             = aws_iam_role.iam_for_meter_counter.arn
  environment {
    variables = {
      COUNTERS_TABLE_NAME = aws_dynamodb_table.customer_counters_table.name
      DDB_REGION          = var.region
    }
  }
}

resource "aws_lambda_function" "counter_info" {
  filename      = local.info_lambda_filepath
  function_name = "meter-counter-info-processor"

  handler     = "edu.demo.meter.counter.info.CounterInfoHandler"
  runtime     = "java8"
  memory_size = "320"
  timeout     = 16

  source_code_hash = filebase64sha256(local.info_lambda_filepath)
  role             = aws_iam_role.iam_for_meter_counter.arn
  environment {
    variables = {
      VILLAGES_TABLE_NAME = aws_dynamodb_table.customer_villages_table.name
      DDB_REGION          = var.region
    }
  }
}

resource "aws_lambda_function" "counter_report" {
  filename      = local.report_lambda_filepath
  function_name = "meter-counter-report-processor"

  handler     = "edu.demo.meter.counter.report.CounterReportHandler"
  runtime     = "java8"
  memory_size = "1024"
  timeout     = 16

  source_code_hash = filebase64sha256(local.report_lambda_filepath)
  role             = aws_iam_role.iam_for_meter_counter.arn
  environment {
    variables = {
      DDB_REGION = var.region
    }
  }
}

resource "aws_lambda_permission" "apigw_post_counter" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.counter_callback.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_api_gateway_rest_api.demo_meter_counter_api.execution_arn}/*/*"
}

resource "aws_lambda_permission" "apigw_get_info" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.counter_info.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_api_gateway_rest_api.demo_meter_counter_api.execution_arn}/*/*"
}

resource "aws_lambda_permission" "apigw_get_report" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.counter_report.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_api_gateway_rest_api.demo_meter_counter_api.execution_arn}/*/*"
}