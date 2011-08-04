
package com.pugh.sockso;

import com.pugh.sockso.db.Database;
import com.pugh.sockso.db.MySQLDatabase;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.pugh.sockso.auth.Authenticator;
import com.pugh.sockso.auth.DBAuthenticator;
import com.pugh.sockso.db.HSQLDatabase;
import com.pugh.sockso.db.SQLiteDatabase;
import com.pugh.sockso.music.CollectionManager;
import com.pugh.sockso.music.DBCollectionManager;
import com.pugh.sockso.music.indexing.Indexer;
import com.pugh.sockso.music.indexing.TrackIndexer;
import com.pugh.sockso.resources.FileResources;
import com.pugh.sockso.resources.JarResources;
import com.pugh.sockso.resources.Resources;

import joptsimple.OptionSet;

import org.apache.log4j.Logger;

public class SocksoModule extends AbstractModule {
    
    private static final Logger log = Logger.getLogger( SocksoModule.class );
    
    private final OptionSet options;
    
    public SocksoModule( final OptionSet options ) {
        
        this.options = options;
        
    }
    
    @Override
    protected void configure() {
        
        configureDatabase();
        configureResources();
        configureProperties();
        configureIndexer();
        configureCollectionManager();
        configureAuthenticator();
        
    }
    
    private void configureDatabase() {
        
        final String dbtype = options.has( Options.OPT_DBTYPE )
            ? options.valueOf(Options.OPT_DBTYPE).toString() : "";

        if ( dbtype.equals("mysql") ) {
            log.info( "Using MySQL Database" );
            bind( Database.class ).to( MySQLDatabase.class );
        }
        
        if ( dbtype.equals("sqlite") ) {
            log.info( "Using sqlite Database" );
            bind( Database.class ).to( SQLiteDatabase.class );
        }

        else {
            log.info( "Using HSQL Database" );
            bind( Database.class ).to( HSQLDatabase.class );
        }
        
    }
    
    private void configureResources() {
        
        final String resourceType = options.has( Options.OPT_RESOURCESTYPE )
            ? options.valueOf(Options.OPT_RESOURCESTYPE).toString() : "";

        log.debug( "Resources type: " +resourceType );
        
        if ( resourceType.equals( "jar" ) ) {
            bind( Resources.class ).to( JarResources.class );
        }
        
        else {
            bind( Resources.class ).to( FileResources.class );
        }
        
    }
    
    private void configureProperties() {
        
        bind( Properties.class ).to( DBProperties.class );
        
    }
    
    private void configureIndexer() {
        
        bind( Indexer.class ).to( TrackIndexer.class );
        
    }
    
    private void configureCollectionManager() {
        
        bind( CollectionManager.class ).to( DBCollectionManager.class );
        
    }
    
    private void configureAuthenticator() {
        
        bind( Authenticator.class ).to( DBAuthenticator.class );
        
    }
    
}
