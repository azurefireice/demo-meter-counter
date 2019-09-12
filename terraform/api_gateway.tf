locals {
  default_content_handling     = "CONVERT_TO_TEXT"
  default_passthrough_behavior = "WHEN_NO_MATCH"
  default_response_templates = {
    "application/json" = "$input.json('$')"
  }
  default_error_template = {
    "application/json" = "$input.path('$.errorMessage')"
  }
}


resource "aws_api_gateway_rest_api" "demo_meter_counter_api" {
  name = "DemoMeterCounter"
}

resource "aws_api_gateway_resource" "counter_root" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  parent_id   = aws_api_gateway_rest_api.demo_meter_counter_api.root_resource_id
  path_part   = "electricity_counter"
}

###                     ###
### Counter_callback    ###
###                     ###
resource "aws_api_gateway_resource" "counter_callback_resource" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  parent_id   = aws_api_gateway_resource.counter_root.id
  path_part   = "counter_callback"
}

resource "aws_api_gateway_method" "counter_callback_method" {
  rest_api_id   = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id   = aws_api_gateway_resource.counter_callback_resource.id
  http_method   = "POST"
  authorization = "NONE"
}

resource "aws_api_gateway_method_response" "counter_callback_response_200" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id = aws_api_gateway_resource.counter_callback_resource.id
  http_method = aws_api_gateway_method.counter_callback_method.http_method
  status_code = "200"
}

resource "aws_api_gateway_method_response" "counter_callback_response_500" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id = aws_api_gateway_resource.counter_callback_resource.id
  http_method = aws_api_gateway_method.counter_callback_method.http_method
  status_code = "500"
}

resource "aws_api_gateway_integration_response" "counter_callback_response_200_integration" {
  depends_on = [
    "aws_api_gateway_integration.counter_callback_lambda_integration",
  ]
  rest_api_id        = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id        = aws_api_gateway_resource.counter_callback_resource.id
  http_method        = aws_api_gateway_method.counter_callback_method.http_method
  status_code        = aws_api_gateway_method_response.counter_callback_response_200.status_code
  selection_pattern  = ""
  response_templates = local.default_response_templates
  content_handling   = local.default_content_handling
}

resource "aws_api_gateway_integration_response" "counter_callback_response_500_integration" {
  depends_on = [
    "aws_api_gateway_integration.counter_callback_lambda_integration",
  ]
  rest_api_id        = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id        = aws_api_gateway_resource.counter_callback_resource.id
  http_method        = aws_api_gateway_method.counter_callback_method.http_method
  status_code        = aws_api_gateway_method_response.counter_callback_response_500.status_code
  selection_pattern  = ".*error.*"
  response_templates = local.default_response_templates
  content_handling   = local.default_content_handling
}

resource "aws_api_gateway_integration" "counter_callback_lambda_integration" {
  rest_api_id             = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id             = aws_api_gateway_resource.counter_callback_resource.id
  http_method             = aws_api_gateway_method.counter_callback_method.http_method
  integration_http_method = aws_api_gateway_method.counter_callback_method.http_method

  type                 = "AWS"
  uri                  = aws_lambda_function.counter_callback.invoke_arn
  request_templates    = local.default_response_templates
  passthrough_behavior = local.default_passthrough_behavior
  content_handling     = local.default_content_handling
}

###                     ###
### Counter             ###
###                     ###
resource "aws_api_gateway_resource" "counter_info_resource" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  parent_id   = aws_api_gateway_resource.counter_root.id
  path_part   = "counter"
}

resource "aws_api_gateway_method" "counter_info_method" {
  rest_api_id   = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id   = aws_api_gateway_resource.counter_info_resource.id
  http_method   = "GET"
  authorization = "NONE"
  request_parameters = {
    "method.request.querystring.id" = true
  }
}

resource "aws_api_gateway_method_response" "counter_info_response_200" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id = aws_api_gateway_resource.counter_info_resource.id
  http_method = aws_api_gateway_method.counter_info_method.http_method
  status_code = "200"
}

resource "aws_api_gateway_method_response" "counter_info_response_400" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id = aws_api_gateway_resource.counter_info_resource.id
  http_method = aws_api_gateway_method.counter_info_method.http_method
  status_code = "400"
}


resource "aws_api_gateway_method_response" "counter_info_response_404" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id = aws_api_gateway_resource.counter_info_resource.id
  http_method = aws_api_gateway_method.counter_info_method.http_method
  status_code = "404"
}

resource "aws_api_gateway_method_response" "counter_info_response_500" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id = aws_api_gateway_resource.counter_info_resource.id
  http_method = aws_api_gateway_method.counter_info_method.http_method
  status_code = "500"
}

resource "aws_api_gateway_integration_response" "counter_info_response_200_integration" {
  depends_on = [
    "aws_api_gateway_integration.counter_info_lambda_integration",
  ]
  rest_api_id        = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id        = aws_api_gateway_resource.counter_info_resource.id
  http_method        = aws_api_gateway_method.counter_info_method.http_method
  status_code        = aws_api_gateway_method_response.counter_info_response_200.status_code
  selection_pattern  = ""
  response_templates = local.default_response_templates
  content_handling   = local.default_content_handling
}

resource "aws_api_gateway_integration_response" "counter_info_response_400_integration" {
  depends_on = [
    "aws_api_gateway_integration.counter_info_lambda_integration",
  ]
  rest_api_id        = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id        = aws_api_gateway_resource.counter_info_resource.id
  http_method        = aws_api_gateway_method.counter_info_method.http_method
  status_code        = aws_api_gateway_method_response.counter_info_response_400.status_code
  selection_pattern  = ".*Counter id is invalid or empty.*"
  response_templates = local.default_error_template
  content_handling   = local.default_content_handling
}

resource "aws_api_gateway_integration_response" "counter_info_response_404_integration" {
  depends_on = [
    "aws_api_gateway_integration.counter_info_lambda_integration",
  ]
  rest_api_id        = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id        = aws_api_gateway_resource.counter_info_resource.id
  http_method        = aws_api_gateway_method.counter_info_method.http_method
  status_code        = aws_api_gateway_method_response.counter_info_response_404.status_code
  selection_pattern  = ".*Item not found.*"
  response_templates = local.default_error_template
  content_handling   = local.default_content_handling
}

resource "aws_api_gateway_integration_response" "counter_info_response_500_integration" {
  depends_on = [
    "aws_api_gateway_integration.counter_info_lambda_integration",
  ]
  rest_api_id        = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id        = aws_api_gateway_resource.counter_info_resource.id
  http_method        = aws_api_gateway_method.counter_info_method.http_method
  status_code        = aws_api_gateway_method_response.counter_info_response_500.status_code
  selection_pattern  = ".*(error|exception).*"
  response_templates = local.default_error_template
  content_handling   = local.default_content_handling
}

resource "aws_api_gateway_integration" "counter_info_lambda_integration" {
  rest_api_id             = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id             = aws_api_gateway_resource.counter_info_resource.id
  http_method             = aws_api_gateway_method.counter_info_method.http_method
  integration_http_method = "POST"

  type = aws_api_gateway_integration.counter_callback_lambda_integration.type
  uri  = aws_lambda_function.counter_info.invoke_arn
  request_parameters = {
    "integration.request.querystring.counterId" = "method.request.querystring.id"
  }
  request_templates = {
    "application/json" = <<EOF
{ "counterId": "$input.params('id')" }
EOF
  }
  passthrough_behavior = local.default_passthrough_behavior
  content_handling     = local.default_content_handling
}

###                     ###
### consumption_report  ###
###                     ###
resource "aws_api_gateway_resource" "counter_report_resource" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  parent_id   = aws_api_gateway_resource.counter_root.id
  path_part   = "consumption_report"
}

resource "aws_api_gateway_method" "counter_report_method" {
  rest_api_id   = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id   = aws_api_gateway_resource.counter_report_resource.id
  http_method   = "GET"
  authorization = "NONE"
  request_parameters = {
    "method.request.querystring.duration" = true
  }
}

resource "aws_api_gateway_method_response" "counter_report_response_200" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id = aws_api_gateway_resource.counter_report_resource.id
  http_method = aws_api_gateway_method.counter_report_method.http_method
  status_code = "200"
}

resource "aws_api_gateway_method_response" "counter_report_response_400" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id = aws_api_gateway_resource.counter_report_resource.id
  http_method = aws_api_gateway_method.counter_report_method.http_method
  status_code = "400"
}

resource "aws_api_gateway_method_response" "counter_report_response_500" {
  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id = aws_api_gateway_resource.counter_report_resource.id
  http_method = aws_api_gateway_method.counter_report_method.http_method
  status_code = "500"
}

resource "aws_api_gateway_integration_response" "counter_report_response_200_integration" {
  depends_on = [
    "aws_api_gateway_integration.counter_report_lambda_integration",
  ]
  rest_api_id        = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id        = aws_api_gateway_resource.counter_report_resource.id
  http_method        = aws_api_gateway_method.counter_report_method.http_method
  status_code        = aws_api_gateway_method_response.counter_report_response_200.status_code
  selection_pattern  = ""
  response_templates = local.default_response_templates
  content_handling   = local.default_content_handling
}

resource "aws_api_gateway_integration_response" "counter_report_response_400_integration" {
  depends_on = [
    "aws_api_gateway_integration.counter_report_lambda_integration",
  ]
  rest_api_id        = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id        = aws_api_gateway_resource.counter_report_resource.id
  http_method        = aws_api_gateway_method.counter_report_method.http_method
  status_code        = aws_api_gateway_method_response.counter_report_response_400.status_code
  selection_pattern  = ".*Duration is invalid or empty.*"
  response_templates = local.default_error_template
  content_handling   = local.default_content_handling
}

resource "aws_api_gateway_integration_response" "counter_report_response_500_integration" {
  depends_on = [
    "aws_api_gateway_integration.counter_report_lambda_integration",
  ]
  rest_api_id        = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id        = aws_api_gateway_resource.counter_report_resource.id
  http_method        = aws_api_gateway_method.counter_report_method.http_method
  status_code        = aws_api_gateway_method_response.counter_report_response_500.status_code
  selection_pattern  = ".*Exception.*"
  response_templates = local.default_error_template
  content_handling   = local.default_content_handling
}

resource "aws_api_gateway_integration" "counter_report_lambda_integration" {
  rest_api_id             = aws_api_gateway_rest_api.demo_meter_counter_api.id
  resource_id             = aws_api_gateway_resource.counter_report_resource.id
  http_method             = aws_api_gateway_method.counter_report_method.http_method
  integration_http_method = "POST"

  type = aws_api_gateway_integration.counter_callback_lambda_integration.type
  uri  = aws_lambda_function.counter_report.invoke_arn
  request_parameters = {
    "integration.request.querystring.duration" = "method.request.querystring.duration"
  }
  request_templates = {
    "application/json" = <<EOF
{ "duration": "$input.params('duration')" }
EOF
  }
  passthrough_behavior = local.default_passthrough_behavior
  content_handling     = local.default_content_handling
}


###                     ###
###     Deployment      ###
###                     ###
resource "aws_api_gateway_deployment" "demo_meter_counter_api_deployment" {
  depends_on = [
    "aws_api_gateway_integration.counter_callback_lambda_integration",
    "aws_api_gateway_integration.counter_info_lambda_integration",
    "aws_api_gateway_integration.counter_report_lambda_integration",
  ]

  rest_api_id = aws_api_gateway_rest_api.demo_meter_counter_api.id
  stage_name  = "demo"
}

output "base_url" {
  value = "${aws_api_gateway_deployment.demo_meter_counter_api_deployment.invoke_url}/electricity_counter/consumption_report"
}