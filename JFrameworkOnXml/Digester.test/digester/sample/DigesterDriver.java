package digester.sample;
import org.apache.commons.digester3.Digester;

import java.io.*;

public class DigesterDriver {

   public static void main( String[] args ) {

      try {
         Digester digester = new Digester();
         digester.setValidating( false );

         digester.addObjectCreate( "catalog", Catalog.class );

         digester.addObjectCreate( "catalog/book", Book.class );
         digester.addBeanPropertySetter( "catalog/book/author", "author" );
         digester.addBeanPropertySetter( "catalog/book/title", "title" );
         digester.addSetNext( "catalog/book", "addBook" );

         digester.addObjectCreate( "catalog/magazine", Magazine.class );
         digester.addBeanPropertySetter( "catalog/magazine/name", "name" );

         digester.addObjectCreate( "catalog/magazine/article", Article.class );
         digester.addSetProperties( "catalog/magazine/article", "page", "page" );
         digester.addBeanPropertySetter( "catalog/magazine/article/headline" ); 
         digester.addSetNext( "catalog/magazine/article", "addArticle" );

         digester.addSetNext( "catalog/magazine", "addMagazine" );

         File input = new File(DigesterDriver.class.getResource("catalog.xml").getFile());
         Catalog c = (Catalog)digester.parse( input );

         System.out.println( c.toString() );

      } catch( Exception exc ) {
         exc.printStackTrace();
      }
   }
}