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

#include "Debug.h"

#include <Windows.h>
#include <stdio.h>
#include <stdarg.h>

#include "Spotify/Core/Mutex.h"

namespace Spotify { namespace Debug
{
	namespace
	{
		Core::Mutex g_mutex;
	}

	void PrintLine( const char* msg, ... )
	{
		Core::ScopedLock autoLock( &g_mutex );

		char buffer[256];
		
		va_list args;
		va_start (args, msg);
		int numChars = vsprintf (buffer,msg, args);
		perror (buffer);
		va_end (args);

		buffer[numChars] = '\n';
		buffer[numChars+1] = 0;

		OutputDebugStringA( buffer );
	}

	void PrintLine( int indent, const char* msg, ... )
	{
		Core::ScopedLock autoLock( &g_mutex );

		char buffer[256];

		for (int i=0; i<indent; i++)
		{
			buffer[i] = ' ';
		}			
		
		va_list args;
		va_start (args, msg);
		int numChars = vsprintf (buffer+indent,msg, args) + indent;
		perror (buffer);
		va_end (args);

		buffer[numChars] = '\n';
		buffer[numChars+1] = 0;

		OutputDebugStringA( buffer );
		
	}

} // Debug
} // Spotify
