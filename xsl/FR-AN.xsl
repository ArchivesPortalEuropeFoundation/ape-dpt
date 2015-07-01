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

    <xsl:template match="eadid" name="addEadid" mode="copy">
        <eadid>
            <xsl:if test="not(@identifier) and not(@IDENTIFIER) and text()[string-length(normalize-space(.)) ge 1] and normalize-space($mainagencycode)">
                <xsl:attribute name="identifier">
                    <xsl:value-of select="$mainagencycode"/>
                    <xsl:text>_</xsl:text>
                    <xsl:value-of select="text()"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="not(@countrycode) and normalize-space($countrycode)">
                <xsl:attribute name="countrycode" select="$countrycode"/>
            </xsl:if>
            <xsl:if test="@countrycode">
                <xsl:attribute name="countrycode" select="upper-case(@countrycode)"/>
            </xsl:if>
            <xsl:if test="not(@mainagencycode) and normalize-space($mainagencycode)">
                <xsl:attribute name="mainagencycode" select="$mainagencycode"/>
            </xsl:if>
            <xsl:if test="@mainagencycode">
                <xsl:variable name="myCode">
                    <xsl:value-of select="ape:normalizeDate(@mainagencycode, 'mainagencycode')"/>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="normalize-space($myCode)">
                        <xsl:attribute name="mainagencycode">
                            <xsl:value-of select="$myCode"/>
                        </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test="@countrycode">
                            <xsl:variable name="myCode_2">
                                <xsl:value-of
                                        select="ape:normalizeDate(concat(concat(@countrycode,'-'),@mainagencycode), 'mainagencycode')"
                                        />
                            </xsl:variable>
                            <xsl:choose>
                                <xsl:when test="normalize-space($myCode_2)">
                                    <xsl:attribute name="mainagencycode">
                                        <xsl:value-of select="$myCode_2"/>
                                    </xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:if test="normalize-space($mainagencycode)">
                                        <xsl:attribute name="mainagencycode">
                                            <xsl:value-of select="$mainagencycode"/>
                                        </xsl:attribute>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
            <xsl:if test="not(@url)">
                <xsl:attribute name="url" select="'TODO'"/>
            </xsl:if>
            <xsl:if test="@url">
                <xsl:variable name="daolink" select="ape:checkLink(@url)"/>
                <xsl:if test="normalize-space($daolink) != ''">
                    <xsl:attribute name="url" select="$daolink"/>
                </xsl:if>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="not(text())">
                    <xsl:value-of select="$eadidmissing"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="text()" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
        </eadid>
    </xsl:template>

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
            <xsl:for-each select="unitid">
                <xsl:call-template name="unitid"/>
            </xsl:for-each>
            <xsl:apply-templates select="node()[not(local-name() = 'abstract') and not(local-name() = 'scopecontent') and not(local-name() = 'bioghist') and not(local-name() = 'unitid')]" mode="#current"/>
            <xsl:for-each select="following-sibling::note">
                <xsl:call-template name="note"/>
            </xsl:for-each>
            <xsl:for-each select="following-sibling::descgrp/p">
                <xsl:call-template name="note"/>
            </xsl:for-each>
            <xsl:for-each select="parent::c/daogrp/daoloc">
                <xsl:call-template name="daogrp" />
            </xsl:for-each>
        </did>
        <xsl:if test="not(controlaccess) and (descendant::geogname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::subject[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::famname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::persname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::corpname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::occupation[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::genreform[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::function[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::title[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::name[parent::item|parent::entry|parent::p|parent::unittitle])">
            <xsl:call-template name="createControlaccess">
                <xsl:with-param name="context" select=".."/>
            </xsl:call-template>
        </xsl:if>
        <xsl:apply-templates select="abstract | scopecontent | bioghist" mode="#current" />
    </xsl:template>

    <xsl:template name="unitid">
        <xsl:choose>
            <xsl:when test="(@type=&quot;cote d'archives&quot; or @type='call number' or @type='ABS' or @type='bestellnummer' or @type='Bestellnummer' or @type='series_code' or @type='reference' or @type='Sygnatura' or @type='REFERENCE_CODE' or @type='cote-de-consultation' or @type='cote-groupee' or @type='identifiant' or @type='cote' or @type='persistent' or (not(@type))) and (text()[string-length(normalize-space(.)) ge 1] or exists(extptr))">
                <xsl:choose>
                    <xsl:when test="@type = 'cote-groupee' and (following-sibling::unitid or preceding-sibling::unitid)"/>
                    <xsl:otherwise>
                        <unitid type="call number" encodinganalog="3.1.1">
                            <xsl:call-template name="extptr_an"/>
                            <xsl:choose>
                                <xsl:when test="starts-with(string-join(., ''), string-join(//archdesc/did/unitid[1]/text(), '')) or //archdesc/did/unitid[@label='Cotes extrêmes']">
                                    <xsl:value-of select="text()"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="concat(string-join(//archdesc/did/unitid[1]/text(), ''), concat(' - ', .))" />
                                </xsl:otherwise>
                            </xsl:choose>
                        </unitid>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="@type='former call number' or @type='obsolete' or @type='former' or @type='altsignatur' or @type='Altsignatur' or @type='Sygnatura dawna' or @type='ancienne_cote' or @type='OLD_REFERENCE_CODE' or @type='nouvelle-foliotation' or @type='cote-forgée'">
                <xsl:if test="text()[string-length(normalize-space(.)) ge 1]">
                    <unitid type="former call number" encodinganalog="3.1.1">
                        <xsl:call-template name="extptr_an"/>
                        <xsl:apply-templates select="node()" mode="#current"/>
                    </unitid>
                </xsl:if>
            </xsl:when>
            <xsl:when test="@type='file reference' or @type='code-de-communication' or @type='cote de communication' or @type='Aktenzeichen' or @type='aktenzeichen' or @type='Znak teczki'">
                <xsl:if test="text()[string-length(normalize-space(.)) ge 1]">
                    <unitid type="file reference" encodinganalog="3.1.1">
                        <xsl:call-template name="extptr_an"/>
                        <xsl:apply-templates select="node()" mode="#current"/>
                    </unitid>
                </xsl:if>
            </xsl:when>
            <xsl:when test="@type='blank'"/>
            <xsl:otherwise>
                <xsl:call-template name="excludeElement"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="extptr_an">
        <extptr>
            <xsl:attribute name="xlink:href">
                <xsl:value-of select="'TODO'"/>
            </xsl:attribute>
        </extptr>
    </xsl:template>

    <xsl:template name="daogrp">
        <dao>
            <xsl:attribute name="xlink:href">
                <xsl:value-of select="concat(concat('https://www.siv.archives-nationales.culture.gouv.fr/siv/rechercheconsultation/consultation/multimedia/Galerie.action?mediaParam==?UTF-8?B?', ape:base64encoder(@href)), '?=&amp;udTitle==?UTF-8?B?UExBTi1ERS1MQS1UT1VS?=&amp;xpointer=&amp;mmName')" />
            </xsl:attribute>
            <xsl:if test="parent::c/did/unittitle">
                <xsl:attribute name="xlink:title">
                    <xsl:value-of select="parent::c/did/unittitle[1]"/>
                </xsl:attribute>
            </xsl:if>
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