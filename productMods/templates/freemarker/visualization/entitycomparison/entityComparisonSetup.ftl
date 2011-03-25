<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#assign standardVisualizationURLRoot ="/visualization">
<#assign ajaxVisualizationURLRoot ="/visualizationAjax">
<#assign dataVisualizationURLRoot ="/visualizationData">

<#assign organizationURI ="${organizationURI?url}">
<#assign organizationVivoProfileURL = "${urls.base}/individual?uri=${organizationURI}">

<#assign subOrganizationVivoProfileURL = "${urls.base}/individual?">

<#assign subOrganizationGrantTemporalGraphCommonURL = "${urls.base}${standardVisualizationURLRoot}?vis=entity_grant_count">
<#assign subOrganizationPublicationTemporalGraphCommonURL = "${urls.base}${standardVisualizationURLRoot}?vis=entity_comparison">

<#assign organizationPublicationTemporalGraphURL = "${urls.base}${standardVisualizationURLRoot}?vis=entity_comparison&uri=${organizationURI}">
<#assign organizationGrantTemporalGraphURL = "${urls.base}${standardVisualizationURLRoot}?vis=entity_grant_count&uri=${organizationURI}">

<#assign temporalGraphSmallIcon = '${urls.images}/visualization/temporal_vis_small_icon.jpg'>

<#assign temporalGraphDownloadCSVCommonURL = '${urls.base}${dataVisualizationURLRoot}?uri=${organizationURI}&labelField=label'>

<#assign publicationParameter = {   "name": "publication",
                                    "dropDownText": "by Publications", 
                                    "viewLink": "${organizationPublicationTemporalGraphURL}", 
                                    "value": "Publications" }>

<#assign grantParameter = {   "name": "grant",
                              "dropDownText": "by Grants", 
                              "viewLink": "${organizationGrantTemporalGraphURL}", 
                              "value": "Grants" }>
                              
<#assign parameterOptions = [publicationParameter, grantParameter]>

<#assign entityCheckboxSelectorDOMClass = "entity-selector-checkbox">
                                    
<#-- Javascript files -->

<#-- Currently we are using the developer build version for both flot & excanvas libraries,
this is because IE 9 complains about certain properties. After testing it seems that dev 
build version is stable enough. If in next couple of days we feel that there are some issues
we will default to using the stable version unless the request comes from IE 9 in which case
we will use rev 293 (dev build version) of the flot & excanvas files.
-->

${scripts.add('<!--[if IE]><script type="text/javascript" src="${urls.base}/js/visualization/entitycomparison/jquery_plugins/flot/r293/excanvas.min.js"></script><![endif]-->',
              '<script type="text/javascript" src="${urls.base}/js/visualization/entitycomparison/jquery_plugins/flot/r293/jquery.flot.min.js"></script>',
              '<script type="text/javascript" src="${urls.base}/js/visualization/entitycomparison/jquery_plugins/fliptext/jquery.mb.flipText.js"></script>',
              '<script type="text/javascript" src="${urls.base}/js/jquery-ui/js/jquery-ui-1.8.9.custom.min.js"></script>',
              '<script type="text/javascript" src="${urls.base}/js/jquery_plugins/jquery.blockUI.min.js"></script>',
              '<script type="text/javascript" src="${urls.base}/js/jquery_plugins/jquery.dataTables.min.js"></script>', 
              '<script type="text/javascript" src="${urls.base}/js/visualization/entitycomparison/util.js"></script>',
              '<script type="text/javascript" src="${urls.base}/js/visualization/entitycomparison/constants.js"></script>',
              '<script type="text/javascript" src="${urls.base}/js/jquery_plugins/jquery.notify.min.js"></script>')}              

<#-- CSS files -->

${stylesheets.add('<link rel="stylesheet" href="${urls.base}/js/jquery-ui/css/smoothness/jquery-ui-1.8.9.custom.css" />',
                  '<link rel="stylesheet" href="${urls.base}/js/visualization/entitycomparison/jquery_plugins/datatable/demo_table.css" />',
                  '<link rel="stylesheet" href="${urls.base}/css/visualization/entitycomparison/layout.css" />',
                  '<link rel="stylesheet" href="${urls.base}/css/visualization/visualization.css" />',
                  '<link rel="stylesheet" href="${urls.base}/css/jquery_plugins/ui.notify.css" />',
                  '<!--[if IE]><link href="${urls.base}/css/visualization/entitycomparison/layout-ie.css" rel="stylesheet" type="text/css" /><![endif]-->',
                  '<!--[if lt IE 8]><link href="${urls.base}/css/visualization/entitycomparison/layout-ie-67.css" rel="stylesheet" type="text/css" /><![endif]-->')}                  
 
<#-- variables passed from server-side code -->
<script language="JavaScript" type="text/javascript">
    
var contextPath = "${urls.base}";
var temporalGraphSmallIcon = "${temporalGraphSmallIcon}";
var subOrganizationVivoProfileURL = "${subOrganizationVivoProfileURL}";

var jsonString = '${jsonContent!}';
var organizationLabel = '${organizationLabel}';
var organizationVIVOProfileURL = "${organizationVivoProfileURL}";

var loadingImageLink = contextPath + "/images/visualization/ajax-loader-indicator.gif";
var refreshPageImageLink = contextPath + "/images/visualization/refresh-green.png";

var entityCheckboxSelectorDOMClass = "${entityCheckboxSelectorDOMClass}";

var isDataRequestSentViaAJAX = false;

var temporalGraphProcessor;

/*
This has to be declared before making a call to GUI event manager JS.
*/
var COMPARISON_PARAMETERS_INFO = {

<#list parameterOptions as parameter>

    ${parameter.name}: {

    <#list parameter?keys as key>
        ${key}:"${parameter[key]}"<#if key_has_next>,</#if>
    </#list>
    
    }<#if parameter_has_next>,</#if>

</#list>
    
}

</script>

${scripts.add('<script type="text/javascript" src="${urls.base}/js/visualization/entitycomparison/gui-event-manager.js"></script>')}