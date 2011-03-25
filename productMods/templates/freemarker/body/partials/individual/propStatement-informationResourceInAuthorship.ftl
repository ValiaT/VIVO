<#-- $This file is distributed under the terms of the license in /doc/license.txt$ -->

<#-- Custom object property statement view for http://vivoweb.org/ontology/core#informationResourceInAuthorship -->

<#import "lib-sequence.ftl" as s>

<@showAuthorship statement />

<#-- Use a macro to keep variable assignments local; otherwise the values carry over to the
     next statement -->
<#macro showAuthorship statement>
    <#if statement.person??>
        <a href="${profileUrl(statement.person)}">${statement.personName}</a>
    <#else>
        <#-- This shouldn't happen, but we must provide for it -->
        <a href="${profileUrl(statement.authorship)}">missing author</a>
    </#if>
</#macro>