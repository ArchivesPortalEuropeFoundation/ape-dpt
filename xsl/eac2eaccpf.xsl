<?xml version="1.0" encoding="UTF-8"?>
<!--
	EAC beta to EAC-CPF stylesheet
	Early draft version
-->
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-33-4"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xpath-default-namespace="urn:isbn:1-931666-33-4"
                exclude-result-prefixes="xsl fo xs">

    <xsl:output indent="yes" method="xml" />

    <xsl:template match="/">
        <xsl:apply-templates select="node()" />
    </xsl:template>

    <xsl:template match="*:eac">
        <eac-cpf xmlns="urn:isbn:1-931666-33-4" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <xsl:if test="@type">
                <xsl:message>We have eac-cpf@type</xsl:message>
            </xsl:if>
            <xsl:apply-templates select="node()"/>
        </eac-cpf>
    </xsl:template>

    <xsl:template match="*:eacheader">
        <control>
            <xsl:if test="*:eacid">
                <xsl:apply-templates select="*:eacid"/>
            </xsl:if>
            <!-- Those 2 elements might need user's input - or find the correspondence in EAC beta if it exists  -->
            <maintenanceStatus>new</maintenanceStatus>
            <maintenanceAgency>
                <agencyName>SOMETHING</agencyName>
            </maintenanceAgency>
            <xsl:if test="*:languagedecl">
                <xsl:apply-templates select="*:languagedecl"/>
            </xsl:if>
            <xsl:apply-templates select="node()[not(name()='eacid' or name()='languagedecl')]"/>
        </control>
    </xsl:template>

    <xsl:template match="*:eacid">
        <recordId>
            <xsl:apply-templates select="node()"/>
        </recordId>
    </xsl:template>

    <xsl:template match="*:mainhist">
        <maintenanceHistory>
            <xsl:if test="string-length(normalize-space(.)) eq 0">
                <maintenanceEvent>
                    <eventType>created</eventType>
                    <eventDateTime>Undetermined</eventDateTime>
                    <agentType>human</agentType>
                    <agent>Unknown</agent>
                </maintenanceEvent>
            </xsl:if>
            <xsl:apply-templates select="node()"/>
        </maintenanceHistory>
    </xsl:template>

    <xsl:template match="*:sourcedecl">
        <sources>
            <xsl:apply-templates select="node()"/>
        </sources>
    </xsl:template>

    <xsl:template match="*:source">
        <source>
            <sourceEntry>
                <xsl:apply-templates select="node()"/>
            </sourceEntry>
        </source>
    </xsl:template>

    <xsl:template match="*:source/*:title">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:mainevent">
        <maintenanceEvent>
            <xsl:if test="@maintype = 'create'">
                <eventType>created</eventType>
            </xsl:if>
            <xsl:if test="@maintype = 'update'">
                <eventType>updated</eventType>
            </xsl:if>
            <xsl:if test="*:maindate">
                <eventDateTime>
                    <xsl:value-of select="*:maindate/text()"/>
                </eventDateTime>
            </xsl:if>
            <agentType>human</agentType>
            <xsl:choose>
                <xsl:when test="*:maindesc">
                    <agent>
                        <xsl:value-of select="*:maindesc/text()"/>
                    </agent>
                </xsl:when>
                <xsl:when test="parent::*/parent::*/*:authdecl/*:auth">
                    <agent>
                        <xsl:value-of select="parent::*/parent::*/*:authdecl/*:auth"/>
                    </agent>
                </xsl:when>
                <xsl:otherwise>
                    <agent>Unknown</agent>
                </xsl:otherwise>
            </xsl:choose>
        </maintenanceEvent>
    </xsl:template>

    <xsl:template match="*:authdecl">
        <xsl:message>authdecl was found - not copied</xsl:message>
    </xsl:template>

    <xsl:template match="*:ruledecl">
        <xsl:message>ruledecl was found - not copied because I have no idea what it is in eac-cpf</xsl:message>
    </xsl:template>

    <xsl:template match="*:condesc">
        <cpfDescription>
            <xsl:apply-templates select="node()[not(name()='eacrels')]"/>
        </cpfDescription>
    </xsl:template>

    <xsl:template match="*:identity">
        <identity>
            <xsl:if test="*:pershead">
                <entityType>person</entityType>
                <nameEntry>
                    <xsl:apply-templates select="*:pershead/*:part"/>
                </nameEntry>
            </xsl:if>
            <xsl:if test="*:persgrp">
                <entityType>person</entityType>
                <xsl:for-each select="*:persgrp/*:pershead">
                    <nameEntry>
                        <xsl:apply-templates select="*:part"/>
                    </nameEntry>
                </xsl:for-each>
            </xsl:if>
            <xsl:if test="*:corpgrp/*:corphead">
                <entityType>corporateBody</entityType>
                <nameEntry>
                    <xsl:for-each select="*:corpgrp/*:corphead">
                        <xsl:apply-templates select="*:part"/>
                    </xsl:for-each>
                </nameEntry>
            </xsl:if>
            <xsl:if test="*:corphead">
                <entityType>corporateBody</entityType>
                <nameEntry>
                    <xsl:apply-templates select="*:corphead"/>
                </nameEntry>
            </xsl:if>
        </identity>
    </xsl:template>

    <xsl:template match="*:corphead">
        <xsl:choose>
            <xsl:when test="*:part">
                <xsl:apply-templates select="*:part"/>
            </xsl:when>
            <xsl:otherwise>
                <part>
                    <xsl:apply-templates />
                </part>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*:desc">
        <description>
            <xsl:if test="*:persdesc/*:existdesc">
                <existDates>
                    <date>
                        <xsl:apply-templates select="*:persdesc/*:existdesc/*:existdate/text()"/>
                    </date>
                </existDates>
            </xsl:if>
            <xsl:apply-templates select="*:persdesc/*[not(name()='existdesc')]"/>
            <biogHist>
                <xsl:apply-templates select="*:bioghist/*"/>
            </biogHist>
        </description>
    </xsl:template>

    <xsl:template match="*:bioghist/*:bioghist">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:locations">
        <places>
            <xsl:apply-templates select="node()"/>
        </places>
    </xsl:template>

    <xsl:template match="*:location">
        <place>
            <xsl:for-each select="*:place">
                <placeEntry>
                    <xsl:apply-templates />
                </placeEntry>
            </xsl:for-each>
        </place>
    </xsl:template>

    <xsl:template match="*:bioghist/*:p">
        <p>
            <xsl:apply-templates select="node()"/>
        </p>
    </xsl:template>

    <xsl:template match="*:p/*:emph">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:funactdesc">
        <occupation>
            <term>
                <xsl:apply-templates select="*:p/text()"/>
            </term>
        </occupation>
    </xsl:template>

    <xsl:template match="*:languagedecl">
        <languageDeclaration>
            <xsl:apply-templates select="node()"/>
        </languageDeclaration>
    </xsl:template>

    <xsl:template match="*:language">
        <language>
            <xsl:attribute name="languageCode" select="@languagecode"/>
            <xsl:apply-templates select="node()"/>
        </language>
        <script scriptCode="Latn">Latin</script>
    </xsl:template>

    <xsl:template match="*:resourcerels">
        <relations>
            <xsl:if test="parent::*/*:eacrels">
                <xsl:apply-templates select="parent::*/*:eacrels"/>
            </xsl:if>
            <xsl:apply-templates select="node()"/>
        </relations>
    </xsl:template>

    <xsl:template match="*:eacrels">
        <cpfRelation>
            <xsl:apply-templates select="node()"/>
        </cpfRelation>
    </xsl:template>

    <xsl:template match="*:eacrel">
        <relationEntry>
            <xsl:apply-templates select="node()[not(name()='descnote')]"/>
        </relationEntry>
        <xsl:if test="*:descnote">
            <xsl:if test="(string-length(normalize-space(*:descnote)) gt 0) or *:descnote/child::*">
                <descriptiveNote>
                    <xsl:apply-templates select="*:descnote"/>
                </descriptiveNote>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <xsl:template match="*:eacrel/*:date">
        <xsl:if test="string-length(.) gt 0">
            <date>
                <xsl:apply-templates />
            </date>
        </xsl:if>
    </xsl:template>

    <xsl:template match="*:eacrel/*:descnote">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:eacrel/*:descnote/*:address">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:eacrel/*:descnote/*:address/*:addressline">
        <p>
            <xsl:apply-templates select="node()"/>
        </p>
    </xsl:template>

    <xsl:template match="*:eacrel/*:corpname">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:eacrel/*:corpname/*:abbr">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:resourcerel">
        <resourceRelation>
            <xsl:if test="@syskey">
                <xsl:attribute name="xlink:href" select="@syskey"/>
            </xsl:if>
            <xsl:if test="*:archunit/*:unitid[@repositorycode and @identifier]">
                <xsl:attribute name="xlink:href">
                    <xsl:value-of select="*:archunit/*:unitid/@repositorycode"/> - <xsl:value-of select="*:archunit/*:unitid/@identifier"/>
                </xsl:attribute>
            </xsl:if>
            <relationEntry>
                <xsl:apply-templates select="node()"/>
            </relationEntry>
        </resourceRelation>
    </xsl:template>

    <xsl:template match="*:resourcerel/*:archunit">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:resourcerel/*:archunit/*:unittitle">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:resourcerel/*:archunit/*:unitid">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:resourcerel/*:archunit/*:unitdate">
        <xsl:message>archunit/unitdate ... Nothing done?</xsl:message>
    </xsl:template>

    <xsl:template match="*:resourcerel/*:archunit/*:repository">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:resourcerel/*:archunit/*:repository/*:corpname">
        <xsl:text> - </xsl:text><xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:resourcerel/*:bibunit">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:resourcerel/*:bibunit//*">
        <xsl:apply-templates select="node()"/><xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="node()">
        <xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-33-4">
            <xsl:apply-templates select="node()|@*"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="text()|@*" priority="2">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="comment()" priority="3" />

</xsl:stylesheet>
