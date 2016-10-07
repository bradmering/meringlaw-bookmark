package com.company;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;

import java.io.File;
import java.io.IOException;


public class Bookmark {
    PDDocument document = null;

    /**
     * This will print the documents data.
     *
     * @param args The command line arguments.
     *
     * @throws IOException If there is an error parsing the document.
     */
    public static void main( String[] args ) throws IOException
    {
        if( args.length != 1 )
        {
            usage();
        }
        else
        {
            PDDocument document = null;
            try
            {
                document = PDDocument.load( new File(args[0]) );
                Bookmark meta = new Bookmark();
                PDDocumentOutline outline =  document.getDocumentCatalog().getDocumentOutline();
                if( outline != null )
                {
                    meta.printBookmark( outline, "", document );
                }
                else
                {
                    System.out.println( "This document does not contain any bookmarks" );
                }

            }
            finally
            {
                if( document != null )
                {
                    document.save( "output/"+args[0] );
                    document.close();
                }
            }
        }
    }

    /**
     * This will print the usage for this document.
     */
    private static void usage()
    {
        System.err.println( "Usage: java " + Bookmark.class.getName() + " <input-pdf>" );
    }

    /**
     * This will print the documents bookmarks to System.out.
     *
     * @param bookmark The bookmark to print out.
     * @param indentation A pretty printing parameter
     *
     * @throws IOException If there is an error getting the page count.
     */
    public void printBookmark( PDOutlineNode bookmark, String indentation, PDDocument document ) throws IOException
    {
        PDOutlineItem current = bookmark.getFirstChild();
        PDFont font = PDType1Font.HELVETICA_BOLD;
        while( current != null )
        {
            System.out.println( indentation + current.getTitle() );
            PDPage page = current.findDestinationPage(document);
            System.out.println( page );
            System.out.println( document );
            if(page != null) {
                if(current.getTitle().toLowerCase().contains("(page")) {

                    PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
                    contentStream.setFont(font, 12);
                    //contentStream.newLineAtOffset(-5, -5);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(500, 750);
                    contentStream.showText(current.getTitle());
                    contentStream.endText();
                    contentStream.close();
                }

            }
            printBookmark( current, indentation + "    " , document);
            current = current.getNextSibling();
        }

    }
}
