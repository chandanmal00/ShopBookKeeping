<#include "/common/username_check.ftl">
<br>
<#include "/common/trial_software.ftl">
<#if limit??>
   <h6><i>*Results are limited to ${limit}</i></h6>
</#if>
<#--fix for i18n -->
<#setting locale="en_US">