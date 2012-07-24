<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="http://www.archivesportaleurope.eu/profiles/APEnet_EAG/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:ape="http://www.archivesportaleurope.eu/functions"
                xpath-default-namespace="http://www.archivesportaleurope.eu/profiles/APEnet_EAG/"
                exclude-result-prefixes="xsl fo xs ape">

    <xsl:output indent="yes" method="xml" />

    <xsl:template match="*:xml-stylesheet"/>

    <xsl:template match="*:eag">
        <eag xmlns="http://www.archivesportaleurope.eu/profiles/APEnet_EAG/" audience="external">
            <xsl:apply-templates select="node()" mode="inside"/>
        </eag>
    </xsl:template>

    <xsl:template match="node()" mode="inside">
        <xsl:element name="{local-name()}" namespace="http://www.archivesportaleurope.eu/profiles/APEnet_EAG/">
            <xsl:apply-templates select="node()|@*" mode="inside"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="text()|@*" priority="2" mode="inside">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*" mode="inside"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*:language" mode="inside">
        <language>
            <xsl:if test="@langcode">
                <xsl:attribute name="langcode" select="@langcode"/>
            </xsl:if>
            <xsl:if test="@scriptcode">
                <xsl:attribute name="scriptcode" select="@scriptcode"/>
            </xsl:if>
        </language>
    </xsl:template>

    <xsl:template match="*:desc" priority="5" mode="inside">
        <desc>
            <xsl:choose>
                <xsl:when test="*:geogarea">
                    <xsl:apply-templates select="*:geogarea|@*" mode="inside"/>
                </xsl:when>
                <xsl:otherwise>

                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="*:country">
                    <xsl:apply-templates select="*:country|@*" mode="inside"/>
                </xsl:when>
                <xsl:otherwise>

                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="*:firstdem">
                <xsl:apply-templates select="*:firstdem|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:secondem">
                <xsl:apply-templates select="*:secondem|@*" mode="inside"/>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="*:municipality">
                    <xsl:apply-templates select="*:municipality|@*" mode="inside"/>
                </xsl:when>
                <xsl:otherwise>

                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="*:localentity">
                <xsl:apply-templates select="*:localentity|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:street">
                <xsl:apply-templates select="*:street|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:postalcode">
                <xsl:apply-templates select="*:postalcode|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:telephone">
                <xsl:apply-templates select="*:telephone|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:fax">
                <xsl:apply-templates select="*:fax|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:email">
                <xsl:apply-templates select="*:email|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:webpage">
                <xsl:apply-templates select="*:webpage|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:timetable">
                <xsl:apply-templates select="*:timetable|@*" mode="inside"/>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="*:access">
                    <xsl:apply-templates select="*:access|@*" mode="inside"/>
                </xsl:when>
                <xsl:otherwise>

                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="*:resprepositor">
                <xsl:apply-templates select="*:resprepositor|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:repositorhist">
                <xsl:apply-templates select="*:repositorhist|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:repositorfound">
                <xsl:apply-templates select="*:repositorfound|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:repositorsup">
                <xsl:apply-templates select="*:repositorsup|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:repositorguides">
                <xsl:apply-templates select="*:repositorguides|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:adminhierarchy">
                <xsl:apply-templates select="*:adminhierarchy|@*" mode="inside"/>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="*:buildinginfo">
                    <xsl:apply-templates select="*:buildinginfo|@*" mode="inside"/>
                </xsl:when>
                <xsl:otherwise>

                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="*:extent">
                    <xsl:apply-templates select="*:extent|@*" mode="inside"/>
                </xsl:when>
                <xsl:otherwise>

                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="*:organization">
                    <xsl:apply-templates select="*:organization|@*" mode="inside"/>
                </xsl:when>
                <xsl:otherwise>
                    <organization>
                        <descunit level="collection" classcode="0" fathercode="0">
                            <unitid href="none"/>
                            <unittitle/>
                            <date normal="none"/>
                            <extent>
                                <num unit="volume"/>
                            </extent>
                        </descunit>
                    </organization>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="*:techservices">
                    <xsl:apply-templates select="*:techservices|@*" mode="inside"/>
                </xsl:when>
                <xsl:otherwise>

                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="*:automation">
                    <xsl:apply-templates select="*:automation|@*" mode="inside"/>
                </xsl:when>
                <xsl:otherwise>

                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="*:controlaccess">
                <xsl:apply-templates select="*:controlaccess|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:notes">
                <xsl:apply-templates select="*:notes|@*" mode="inside"/>
            </xsl:if>
            <xsl:if test="*:extptr">
                <xsl:apply-templates select="*:extptr|@*" mode="inside"/>
            </xsl:if>
        </desc>
    </xsl:template>

    <xsl:template match="*:emph" mode="inside">
        <xsl:apply-templates select="node()" mode="inside"/>
    </xsl:template>

    <xsl:template match="*:lb" mode="inside"/>

    <xsl:template match="*:access" mode="inside">
        <access question="yes">
            <xsl:choose>
                <xsl:when test="*:restaccess">
                    <xsl:apply-templates select="node()" mode="inside"/>
                </xsl:when>
                <xsl:otherwise>
                    <restaccess>
                        <xsl:apply-templates select="node()" mode="inside"/>
                    </restaccess>
                </xsl:otherwise>
            </xsl:choose>
        </access>
    </xsl:template>

    <xsl:template match="*:date" mode="inside">
        <date>
            <xsl:if test="@normal">
                <xsl:variable name="normal">
                    <xsl:value-of select="ape:normalizeDate(normalize-space(@normal))"/>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="normalize-space($normal)">
                        <xsl:attribute name="normal" select="$normal" />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:attribute name="normal" select="'1970-01-01'" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
            <xsl:apply-templates select="node()|@*[not(name()='normal')]" mode="inside"/>
        </date>
    </xsl:template>

    <xsl:template match="*:techservices" mode="inside">
        <techservices>
            <xsl:apply-templates select="node()|@*" mode="inside"/>
            <xsl:if test="not(*:library)">
                <library question="yes"/>
            </xsl:if>
        </techservices>
    </xsl:template>

    <xsl:template match="*:num" mode="inside">
        <num>
            <xsl:choose>
                <xsl:when test="@unit='kilometers' or not(@unit)">
                    <xsl:attribute name="unit" select="'volume'"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="unit" select="@unit"/>
                </xsl:otherwise>
            </xsl:choose>
        </num>
    </xsl:template>

    <xsl:template match="comment()" priority="3"  mode="inside"/>

</xsl:stylesheet>
