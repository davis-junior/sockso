
package com.pugh.sockso.web.action.browse;

import com.pugh.sockso.Constants;
import com.pugh.sockso.Properties;
import com.pugh.sockso.StringProperties;
import com.pugh.sockso.db.Database;
import com.pugh.sockso.music.Track;
import com.pugh.sockso.music.Artist;
import com.pugh.sockso.tests.TestUtils;
import com.pugh.sockso.tests.TestResponse;
import com.pugh.sockso.tests.SocksoTestCase;
import com.pugh.sockso.tests.TestDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.Vector;

import static org.easymock.EasyMock.*;

public class PopularerTest extends SocksoTestCase {

    public void testGetPopularTracks() throws SQLException {
        
        final Properties p = createMock( Properties.class );
        expect( p.get(Constants.WWW_BROWSE_POPULAR_TRACK_COUNT,20) ).andReturn( 20l );
        replay( p );
        
        final ResultSet rs = createNiceMock( ResultSet.class );
        expect( rs.next() ).andReturn( true );
        expect( rs.next() ).andReturn( true );
        expect( rs.next() ).andReturn( false );
        replay( rs );
        
        final PreparedStatement st = createMock( PreparedStatement.class );
        st.setInt( 1, 20 );
        expect( st.executeQuery() ).andReturn( rs ).times( 1 );
        st.close();
        replay( st );
        
        final Database db = createMock( Database.class );
        expect( db.prepare((String)anyObject()) ).andReturn( st ).times( 1 );
        replay( db );
        
        final Popularer b = new Popularer();
        b.setProperties( p );
        b.setDatabase( db );
        
        final Vector<Track> tracks = b.getPopularTracks();
        
        assertNotNull( tracks  );
        assertEquals( 2, tracks.size() );
        
        verify( db );
        verify( st );
        verify( rs );
        verify( p );
        
    }
    
    public void testGetPopularTracksQuery() throws Exception {
        
        final Database db = new TestDatabase();
        final Popularer b = new Popularer();
        
        b.setProperties( new StringProperties() );
        b.setDatabase( db );
        b.getPopularTracks();
        
    }
    
    public void testShowPopular() throws Exception {
        
        final TestResponse res = new TestResponse();
        final Popularer b = new Popularer();
        final Vector<Track> tracks = new Vector<Track>();
        
        final Artist artist = new Artist( 1, "my artist" );
        final Track track = new Track( artist, null, 1, "my track", "", 1, null );
        tracks.add( track );
        
        b.setResponse( res );
        b.showPopularTracks( tracks );
        
        final String data = res.getOutput();

        assertTrue( data.contains(artist.getName()) );
        assertTrue( data.contains(track.getName()) );
        
    }

}
