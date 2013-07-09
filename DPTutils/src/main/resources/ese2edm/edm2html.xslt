<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:edm="http://www.europeana.eu/schemas/edm/" xmlns:dcterms="http://purl.org/dc/terms/"
                xmlns="http://www.europeana.eu/schemas/ese/" xpath-default-namespace="urn:isbn:1-931666-22-9" xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:xlink="http://www.w3.org/1999/xlink" exclude-result-prefixes="xlink fn" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:ore="http://www.openarchives.org/ore/terms/">
    <xsl:output method="html" indent="yes" version="4.0" name="html"  encoding="UTF-8"/>
    
    <xsl:param name="eseId"></xsl:param>
    <xsl:param name="outputdir"></xsl:param>
    <xsl:param name="startNumber"></xsl:param>
    <xsl:param name="maxItemsPerDirectory"></xsl:param>
    
    <xsl:template match="/">
        <xsl:variable name="firstfiledirname">
            <xsl:call-template name="getDirName">
                <xsl:with-param name="currentRecordsPerDirectory" select="number($startNumber)"/>
                <xsl:with-param name="maxItemsPerDirectory" select="number($maxItemsPerDirectory)"/>
                <xsl:with-param name="position" select="1"/>
                <xsl:with-param name="prefix" select="''"/>
            </xsl:call-template>
        </xsl:variable>		
        <xsl:variable name="listfilename" select="concat($outputdir, '/list.html')" />
        <xsl:variable name="firstfilename" select="concat($outputdir, '/', $firstfiledirname, '/1 ',fn:replace(/rdf:RDF/edm:ProvidedCHO[1]/dc:identifier, '[\\/\*\?:\|%@\[\] \n\t\r]', '_'),'.html')" />
        <xsl:variable name="webfirstfilename" select="concat($outputdir, '/', $firstfiledirname, '/1%20',fn:replace(/rdf:RDF/edm:ProvidedCHO[1]/dc:identifier, '[\\/\*\?:\|%@\[\] \n\t\r]', '_'),'.html')" />				
        <xsl:variable name="weblistfilename" select="concat($outputdir, '/list.html')" />

        <html>
            <head>
                <title>
                    <xsl:value-of select="tokenize(document-uri(.), '/')[last()]"/>
                </title>
                <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8"/>
            </head>
            <FRAMESET>
				
                <FRAMESET ROWS="100%" COLS="30%,*">
                    <FRAME SRC="{$weblistfilename}" NAME="sitebar"/>
                    <FRAME SRC="{$webfirstfilename}" NAME="main"/>
                </FRAMESET>
				
            </FRAMESET>
        </html>
        <xsl:result-document href="{$listfilename}" method="html" encoding="UTF-8" version="4.0" omit-xml-declaration="yes" >
            <html>
                <head>
                    <link rel="stylesheet" type="text/css" href="europeana/css/epf-common.css"/>
                    <title>
                        <xsl:value-of select="./dc:title" />
                    </title>
                    <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8"/>
                </head>
                <body>
                    <h2>
                        <xsl:value-of select="count(/rdf:RDF/edm:ProvidedCHO) "/>
                        <xsl:text> records in </xsl:text>
                        <xsl:value-of select="tokenize(document-uri(.), '/')[last()]"/>
                    </h2>
                    <xsl:if test="count(/rdf:RDF/edm:ProvidedCHO) > 0">
                        <table cellspacing="1" cellpadding="0" width="100%" border="0" summary="search results" class="list">
                            <th>Identifier</th>
                            <th>Title</th>
                            <xsl:for-each select="/rdf:RDF/edm:ProvidedCHO">
                                <xsl:variable name="dirname">
                                    <xsl:call-template name="getDirName">
                                        <xsl:with-param name="currentRecordsPerDirectory" select="$startNumber"/>
                                        <xsl:with-param name="maxItemsPerDirectory" select="$maxItemsPerDirectory"/>
                                        <xsl:with-param name="position" select="position()"/>
                                        <xsl:with-param name="prefix" select="''"/>
                                    </xsl:call-template>
                                </xsl:variable>							
                                <tr>
                                    <xsl:variable name="filename"
                                                  select="concat($outputdir, '/',$dirname, '/', position(), '%20',fn:replace(./dc:identifier, '[\\/\*\?:\|%@\[\] \n\t\r]', '_'),'.html')" />
                                    <td>
                                        <a href="{$filename}" target="main">
                                            <xsl:value-of select="./dc:identifier" />
                                        </a>
                                    </td>
                                    <td> 
                                        <xsl:value-of select="./dc:title" />
                                    </td>
                                </tr>
                            </xsl:for-each>					
                        </table>
                    </xsl:if>					
                </body>
            </html>			
        </xsl:result-document>		
        <xsl:for-each select="/rdf:RDF/edm:ProvidedCHO">
            <xsl:variable name="oreAgg" select="preceding-sibling::*[1]"/>
            <xsl:variable name="dirname">
                <xsl:call-template name="getDirName">
                    <xsl:with-param name="currentRecordsPerDirectory" select="$startNumber"/>
                    <xsl:with-param name="maxItemsPerDirectory" select="$maxItemsPerDirectory"/>
                    <xsl:with-param name="position" select="position()"/>
                    <xsl:with-param name="prefix" select="''"/>
                </xsl:call-template>
            </xsl:variable>		
            <xsl:variable name="cssdirname">
                <xsl:call-template name="getCSSDirName">
                    <xsl:with-param name="currentRecordsPerDirectory" select="$startNumber"/>
                    <xsl:with-param name="maxItemsPerDirectory" select="$maxItemsPerDirectory"/>
                    <xsl:with-param name="position" select="position()"/>
                    <xsl:with-param name="cssPrefix" select="'../'"/>
                </xsl:call-template>
            </xsl:variable>											
            <xsl:variable name="filename"
                          select="concat($outputdir, '/', $dirname, '/', position(), ' ',fn:replace(./dc:identifier, '[\\/\*\?:\|%@\[\] \n\t\r]', '_'),'.html')" />
            <xsl:result-document href="{$filename}" method="html" encoding="UTF-8" version="4.0" omit-xml-declaration="yes" >
                <html>
                    <head>
                        <link rel="stylesheet" type="text/css" href="europeana/css/epf-common.css"/>
                        <title>
                            <xsl:value-of select="./dc:title" />
                        </title>
                        <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8"/>
                    </head>
                    <body>
                        <table  id="multi"  cellspacing="10" cellpadding="10"> 
                            <tr>
                                <td valign="top">
                                    <div style="width: 200px; max-height: 400px; overflow: hidden; text-align: center;">
                                        <xsl:choose>
                                            <xsl:when test="/rdf:RDF/ore:Aggregation[position()]/edm:isShownBy">
                                                <xsl:variable name="href" select="/rdf:RDF/ore:Aggregation[position()]/edm:isShownBy"/>	
                                                <img width="200px" src="{$href}"></img>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:variable name="type" select="fn:lower-case(./edm:type)"/>	
                                                <img width="200px" src="europeana/images/no-{$type}-thumbnail.gif"></img>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </div>									
                                </td>
                                <td>					
                                    <div id="item-detail">
                                        <h2 class="{./edm:type}">
                                            <xsl:value-of select="./dc:title" />
                                        </h2>
                                        <xsl:for-each select="$oreAgg[position()]/*">
                                            <p>
                                                <strong>
                                                    <xsl:value-of select="./name()" />:<xsl:text> </xsl:text>
                                                </strong>
                                                <xsl:choose>
                                                    <xsl:when test='fn:starts-with(.,"http://")'>
                                                        <xsl:variable name="href" select="."/>	
                                                        <a href="{$href}">
                                                            <xsl:value-of select="." />
                                                        </a>
                                                    </xsl:when>
                                                    <xsl:when test='./@rdf:resource'>
                                                        <xsl:choose>
                                                            <xsl:when test='fn:starts-with(./@rdf:resource,"http://")'>
                                                                <xsl:variable name="href" select="./@rdf:resource"/>	
                                                                <a href="{$href}">
                                                                    <xsl:value-of select="./@rdf:resource" />
                                                                </a>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <xsl:value-of select="./@rdf:resource" />
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:value-of select="." />
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </p>
                                        </xsl:for-each>
                                        <xsl:for-each select="child::*">
                                            <p>
                                                <strong>
                                                    <xsl:value-of select="./name()" />:<xsl:text> </xsl:text>
                                                </strong>
                                                <xsl:choose>
                                                    <xsl:when test='fn:starts-with(.,"http://")'>
                                                        <xsl:variable name="href" select="."/>	
                                                        <a href="{$href}">
                                                            <xsl:value-of select="." />
                                                        </a>
                                                    </xsl:when>
                                                    <xsl:when test='./@rdf:resource'>
                                                        <xsl:value-of select="./@rdf:resource" />
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:value-of select="." />
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </p>
                                        </xsl:for-each>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </body>
                </html>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="getDirName">
        <xsl:param name="currentRecordsPerDirectory"/>
        <xsl:param name="maxItemsPerDirectory"/>
        <xsl:param name="position"/>
        <xsl:param name="prefix"/>
        <xsl:variable name="temp" select="floor($position div number($currentRecordsPerDirectory)) * number($currentRecordsPerDirectory)"></xsl:variable>
	
        <xsl:choose>
            <xsl:when test="number($currentRecordsPerDirectory) > number($maxItemsPerDirectory)">
                <xsl:call-template name="getDirName">
                    <xsl:with-param name="position" select="$position"/>
                    <xsl:with-param name="currentRecordsPerDirectory" select="number($currentRecordsPerDirectory) div number($maxItemsPerDirectory)"/>
                    <xsl:with-param name="maxItemsPerDirectory" select="number($maxItemsPerDirectory)"/>
                    <xsl:with-param name="prefix" select="concat($prefix,$temp,  '/')"/>	
			
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="concat($prefix,  $temp, '/')"/>	
            </xsl:otherwise>	
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="getCSSDirName">
        <xsl:param name="currentRecordsPerDirectory"/>
        <xsl:param name="maxItemsPerDirectory"/>
        <xsl:param name="position"/>
        <xsl:param name="cssPrefix"/>
        <xsl:variable name="temp" select="floor($position div number($currentRecordsPerDirectory)) * number($currentRecordsPerDirectory)"></xsl:variable>
	
        <xsl:choose>
            <xsl:when test="$currentRecordsPerDirectory > $maxItemsPerDirectory">
                <xsl:call-template name="getCSSDirName">
                    <xsl:with-param name="position" select="$position"/>
                    <xsl:with-param name="currentRecordsPerDirectory" select="number($currentRecordsPerDirectory) div number($maxItemsPerDirectory)"/>
                    <xsl:with-param name="maxItemsPerDirectory" select="number($maxItemsPerDirectory)"/>
                    <xsl:with-param name="cssPrefix" select="concat($cssPrefix, '..', '/')"/>					
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="concat($cssPrefix, '..', '/')"/>	
            </xsl:otherwise>	
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>
