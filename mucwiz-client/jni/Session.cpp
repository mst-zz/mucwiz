/*
 * Copyright 2011 Jim Knowler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

// Includes
#include "Spotify/Session.h"

#include "Spotify/Album.h"
#include "Spotify/Artist.h"
#include "Spotify/Image.h"
#include "Spotify/PlayList.h"
#include "Spotify/PlayListContainer.h"
#include "Spotify/PlayListElement.h"
#include "Spotify/PlayListFolder.h"
#include "Spotify/Track.h"

// debugging
// #include "Debug/Debug.h"
#define LOG( msg, ... )	//Debug::PrintLine( msg, __VA_ARGS__ );

namespace Spotify
{
	Session::Config::Config()
	{
		m_appKey = NULL;
		m_appKeySize = 0;
		m_cacheLocation = "";
		m_settingsLocation = "";
		m_userAgent = "";
		m_traceFile = "";
		m_compressPlaylists = true;
		m_dontSaveMetadataForPlaylists = false;
		m_initiallyUnloadPlaylists = false;
	}

	Session::Session() : m_pSession(NULL), m_isProcessEventsRequired(false), m_hasLoggedOut(NULL), m_pTrack(NULL)
	{
	}

	Session::~Session()
	{		
		Shutdown();
	}

	sp_error Session::Initialise( Config& config )
	{
		sp_session_config spConfig;
		memset(&spConfig, 0, sizeof(sp_session_config));
		
		spConfig.api_version = 12;//SPOTIFY_API_VERSION;
		
		// app specified configuration
		spConfig.application_key = config.m_appKey;
		spConfig.application_key_size = config.m_appKeySize;
		spConfig.cache_location = config.m_cacheLocation;
		spConfig.settings_location = config.m_settingsLocation;
		spConfig.user_agent = config.m_userAgent;
		spConfig.tracefile = config.m_traceFile;

		// locally specified configuration
		sp_session_callbacks callbacks;
		memset( &callbacks, 0, sizeof(callbacks) );

//		callbacks.connection_error = callback_connection_error;
//		callbacks.end_of_track = callback_end_of_track;
//		callbacks.get_audio_buffer_stats = callback_get_audio_buffer_stats;
//		callbacks.logged_in = callback_logged_in;
//		callbacks.logged_out = callback_logged_out;
//		callbacks.log_message = callback_log_message;
//		callbacks.message_to_user = callback_message_to_user;
//		callbacks.metadata_updated = callback_metadata_updated;
		callbacks.music_delivery = callback_music_delivery;
//		callbacks.notify_main_thread = callback_notify_main_thread;
//		callbacks.play_token_lost = callback_play_token_lost;
//		callbacks.start_playback = callback_start_playback;
//		callbacks.stop_playback = callback_stop_playback;
//		callbacks.streaming_error = callback_streaming_error;
//		callbacks.userinfo_updated = callback_userinfo_updated;
		
		spConfig.callbacks = &callbacks;
		spConfig.userdata = this;

		spConfig.compress_playlists = config.m_compressPlaylists;
		spConfig.dont_save_metadata_for_playlists = config.m_dontSaveMetadataForPlaylists;
		spConfig.initially_unload_playlists = config.m_initiallyUnloadPlaylists;

		sp_error error = sp_session_create( &spConfig, &m_pSession );
		
		return error;
	}

	void Session::Shutdown()
	{
		if (m_pSession)
		{			
						
			// clear any remaining events
			Update();
			// release the session
			sp_session_release( m_pSession );
			
			m_pSession = NULL;
		}		
	}

	void Session::Update()
	{
		if (m_pSession)
		{						
			m_isProcessEventsRequired = false;
			
			int nextTimeout = 0;
			sp_session_process_events( m_pSession, &nextTimeout );
		}
	}

	void Session::Login( const char* username, const char* password )
	{				
		m_hasLoggedOut = false;
		
		sp_session_login( m_pSession, username, password, false, NULL);
	}

	void Session::Logout()
	{		
		sp_session_logout( m_pSession );
	}

	bool Session::IsLoggedIn()
	{
		bool isloggedIn = (NULL != m_pSession) && !m_hasLoggedOut && (GetConnectionState() == SP_CONNECTION_STATE_LOGGED_IN);
		return isloggedIn;
	}

	sp_connectionstate Session::GetConnectionState()
	{
		if (m_pSession)
		{
			return sp_session_connectionstate(m_pSession);
		}
		else
		{
			return SP_CONNECTION_STATE_LOGGED_OUT;
		}
	}

	sp_error Session::Load( Track* pTrack )
	{
		if (pTrack != m_pTrack)
		{
			if (m_pTrack)
			{
				Unload( m_pTrack );
			}

			if (pTrack)
			{
				sp_error error = sp_session_player_load( m_pSession, pTrack->m_pTrack );
				if (error == SP_ERROR_OK)
				{
					m_pTrack = pTrack;
				}

				return error;
			}
		}

		return SP_ERROR_OK;
	}

	void Session::Unload( Track* pTrack )
	{
		if (pTrack && (pTrack == m_pTrack) )
		{
			sp_session_player_unload( m_pSession );
			m_pTrack = NULL;
		}
	}

	Track* Session::GetCurrentTrack()
	{
		return m_pTrack;
	}

	void Session::Seek( int offset )
	{
		sp_session_player_seek( m_pSession, offset );		
	}

	void Session::Play()
	{
		sp_session_player_play( m_pSession, true );
	}

	void Session::Stop()
	{
		sp_session_player_play( m_pSession, false );
	}
		
	sp_error Session::PreFetch( Track* pTrack )
	{
		sp_error error = sp_session_player_prefetch( m_pSession, pTrack->m_pTrack );
		return error;
	}

	PlayListContainer* Session::GetPlayListContainer()
	{
		sp_playlistcontainer* container = sp_session_playlistcontainer( m_pSession );
		
		if (NULL == container)
		{
			return NULL;
		}

		PlayListContainer* pContainer = CreatePlayListContainer();
		bool isLoading = pContainer->Load( container );

		if (!isLoading)
		{
			delete pContainer;
			pContainer = NULL;
		}

		return pContainer;
	}

	void Session::SetPreferredBitrate( sp_bitrate bitrate )
	{
		sp_session_preferred_bitrate( m_pSession, bitrate );
	}

	PlayList* Session::CreatePlayList()
	{
		return new PlayList( this );
	}

	PlayListContainer* Session::CreatePlayListContainer()
	{
		return new PlayListContainer( this );
	}

	PlayListFolder* Session::CreatePlayListFolder()
	{
		return new PlayListFolder( this );
	}

	Track* Session::CreateTrack()
	{
		return new Track( this );
	}

	Artist* Session::CreateArtist()
	{
		return new Artist( this );
	}

	Album* Session::CreateAlbum()
	{
		return new Album( this );
	}

	Image* Session::CreateImage()
	{
		return new Image( this );
	}
		
	static Session* GetSessionFromUserdata( sp_session* session )
	{
		Session* pSession = reinterpret_cast<Session*>(sp_session_userdata(session));
		return pSession;
	}

	void SP_CALLCONV Session::callback_logged_in(sp_session *session, sp_error error)
	{
		Session* pSession = GetSessionFromUserdata( session );		
		pSession->OnLoggedIn( error );
	}

	void SP_CALLCONV Session::callback_logged_out(sp_session *session)
	{
		Session* pSession = GetSessionFromUserdata( session );
		
		pSession->m_hasLoggedOut = true;
		
		pSession->OnLoggedOut();
	}

	void SP_CALLCONV Session::callback_metadata_updated(sp_session *session)
	{
		Session* pSession = GetSessionFromUserdata( session );
		pSession->OnMetadataUpdated();
	}

	void SP_CALLCONV Session::callback_connection_error(sp_session *session, sp_error error)
	{
		Session* pSession = GetSessionFromUserdata( session );
		pSession->OnConnectionError( error );
	}

	void SP_CALLCONV Session::callback_message_to_user(sp_session *session, const char *message)
	{
		Session* pSession = GetSessionFromUserdata( session );
		pSession->OnMessageToUser( message );
	}

	void SP_CALLCONV Session::callback_notify_main_thread(sp_session *session)
	{		
		Session* pSession = GetSessionFromUserdata( session );				

		pSession->OnNotifyMainThread();		
		
		pSession->m_isProcessEventsRequired = true;		
	}

	int  SP_CALLCONV Session::callback_music_delivery(sp_session *session, const sp_audioformat *format, const void *frames, int num_frames)
	{
		Session* pSession = GetSessionFromUserdata( session );	
		return pSession->OnMusicDelivery( format, frames, num_frames );		
	}

	void SP_CALLCONV Session::callback_play_token_lost(sp_session *session)
	{
		Session* pSession = GetSessionFromUserdata( session );
		pSession->OnPlayTokenLost();
	}

	void SP_CALLCONV Session::callback_log_message(sp_session *session, const char *data)
	{
		Session* pSession = GetSessionFromUserdata( session );
		pSession->OnLogMessage( data );
	}

	void SP_CALLCONV Session::callback_end_of_track(sp_session *session)
	{
		Session* pSession = GetSessionFromUserdata( session );
		pSession->OnEndOfTrack();
	}

	void SP_CALLCONV Session::callback_streaming_error(sp_session *session, sp_error error)
	{
		Session* pSession = GetSessionFromUserdata( session );
		pSession->OnStreamingError( error );
	}

	void SP_CALLCONV Session::callback_userinfo_updated(sp_session *session)
	{
		Session* pSession = GetSessionFromUserdata( session );
		pSession->OnUserinfoUpdated();
	}

	void SP_CALLCONV Session::callback_start_playback(sp_session *session)
	{
		Session* pSession = GetSessionFromUserdata( session );
		pSession->OnStartPlayback();
	}

	void SP_CALLCONV Session::callback_stop_playback(sp_session *session)
	{
		Session* pSession = GetSessionFromUserdata( session );
		pSession->OnStopPlayback();
	}

	void SP_CALLCONV Session::callback_get_audio_buffer_stats(sp_session *session, sp_audio_buffer_stats *stats)
	{
		Session* pSession = GetSessionFromUserdata( session );
		pSession->OnGetAudioBufferStats( stats );
	}

	void Session::OnLoggedIn( sp_error error )
	{
		LOG("Session::OnLoggedIn");
	}

	void Session::OnLoggedOut()
	{
		LOG("Session::OnLoggedOut");
	}

	void Session::OnMetadataUpdated()
	{
		LOG("Session::OnMetadataUpdated");
	}

	void Session::OnConnectionError( sp_error error )
	{
		LOG("Session::OnConnectionError");
	}

	void Session::OnMessageToUser( const char* message )
	{
		LOG("Session::OnMessageToUser");
		LOG( message );
	}

	void Session::OnNotifyMainThread()
	{
		LOG("Session::OnNotifyMainThread");
	}

	int  Session::OnMusicDelivery( const sp_audioformat* format, const void* frames, int num_frames )
	{
		LOG("Session::OnMusicDelivery [%d]", ((int*)frames)[0]);

		// pretend that we have consumed all of the audio frames
		return num_frames;
	}

	void Session::OnPlayTokenLost()
	{
		LOG("Session::OnPlayTokenLost");
	}

	void Session::OnLogMessage( const char* data )
	{
		LOG("Session::OnLogMessage");
		LOG( data );
	}

	void Session::OnEndOfTrack()
	{
		LOG("Session::OnEndOfTrack");
	}

	void Session::OnStreamingError( sp_error error )
	{
		LOG("Session::OnStreamingError");
	}

	void Session::OnUserinfoUpdated()
	{
		LOG("Session::OnUserinfoUpdated");
	}

	void Session::OnStartPlayback()
	{
		LOG("Session::OnStartPlayback");
	}

	void Session::OnStopPlayback()
	{
		LOG("Session::OnStopPlayback");
	}

	void Session::OnGetAudioBufferStats( sp_audio_buffer_stats* stats )
	{
		LOG("Session::OnGetAudioBufferStats");
	}
	
	void Session::GetTrackFromURI(const char* uri)
	{
		sp_link* lnk = sp_link_create_from_string(uri);
		sp_track* track = sp_link_as_track(lnk);

		sp_session_player_load(m_pSession, track);
	    sp_session_player_play(m_pSession, 1);

		return;
	}

}
