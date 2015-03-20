<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:none="none"
                xmlns:ape="http://www.archivesportaleurope.net/functions"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9" exclude-result-prefixes="xsl fo xs none ape">

    <xsl:import href="system/default-apeEAD.xsl"/>

    <xsl:template match="c" mode="copy level">
        <xsl:if test="not(@audience)">
            <c>
                <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
                <xsl:attribute name="id">
                    <xsl:choose>
                        <xsl:when test="normalize-space(@id)">
                            <xsl:value-of select="normalize-space(@id)"/>
                        </xsl:when>
                        <xsl:when test="normalize-space(child::*[name()='did']/@id)">
                            <xsl:value-of select="normalize-space(child::*[name()='did']/@id)"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="generate-id()"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <xsl:apply-templates mode="level"/>
            </c>
        </xsl:if>
    </xsl:template>

    <xsl:template match="did" mode="level">
        <did>
            <xsl:if test="not(unittitle)">
                <unittitle/>
            </xsl:if>
            <xsl:apply-templates select="node()[not(local-name() = 'abstract') and not(local-name() = 'scopecontent') and not(local-name() = 'bioghist')]" mode="#current"/>
            <xsl:for-each select="following-sibling::note">
                <xsl:call-template name="note"/>
            </xsl:for-each>
            <xsl:for-each select="following-sibling::descgrp/p">
                <xsl:call-template name="note"/>
            </xsl:for-each>
        </did>
        <xsl:if test="not(controlaccess) and (descendant::geogname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::subject[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::famname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::persname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::corpname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::occupation[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::genreform[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::function[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::title[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::name[parent::item|parent::entry|parent::p|parent::unittitle])">
            <xsl:call-template name="createControlaccess">
                <xsl:with-param name="context" select=".."/>
            </xsl:call-template>
        </xsl:if>
        <xsl:apply-templates select="abstract | scopecontent | bioghist" mode="#current" />
    </xsl:template>

    <xsl:template match="daogrp/daoloc" mode="copy level">
        <dao>
            <xsl:attribute name="xlink:href">
                <xsl:value-of select="concat(concat('https://www.siv.archives-nationales.culture.gouv.fr/siv/rechercheconsultation/consultation/multimedia/Galerie.action?mediaParam==?UTF-8?B?', ape:base64encoder(@href)), '?=&amp;udTitle==?UTF-8?B?UExBTi1ERS1MQS1UT1VS?=&amp;xpointer=&amp;mmName')" />
            </xsl:attribute>
            <xsl:call-template name="daoRoleType"/>
        </dao>
        <dao>
            <xsl:attribute name="xlink:href">
                <xsl:if test="contains(@href, '#')">
                    <xsl:value-of select="concat('https://www.siv.archives-nationales.culture.gouv.fr/mm/media/download/', replace(substring-before(@href, '#'), '.jpg', ''), '-min.jpg')" />
                </xsl:if>
                <xsl:if test="not(contains(@href, '#'))">
                    <xsl:value-of select="concat('https://www.siv.archives-nationales.culture.gouv.fr/mm/media/download/', replace(@href, '.jpg', ''), '-min.jpg')" />
                </xsl:if>
            </xsl:attribute>
            <xsl:attribute name="xlink:title">
                <xsl:value-of select="'thumbnail'" />
            </xsl:attribute>
        </dao>
    </xsl:template>

</xsl:stylesheet>