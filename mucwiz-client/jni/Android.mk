LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := libspotify
LOCAL_SRC_FILES := lib/libspotify.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := libspotify-plus-plus
LOCAL_SRC_FILES := Album.cpp Artist.cpp PlayList.cpp PlayListFolder.cpp Track.cpp Image.cpp PlayListContainer.cpp PlayListElement.cpp Session.cpp
LOCAL_SHARED_LIBRARIES := libspotify
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := jlibspotify
LOCAL_SRC_FILES := jlibspotify/JAlbum.cpp jlibspotify/JImage.cpp jlibspotify/JPlayList.cpp jlibspotify/JPlayListFolder.cpp jlibspotify/JTrack.cpp jlibspotify/JArtist.cpp jlibspotify/JPlayListContainer.cpp jlibspotify/JPlayListElement.cpp jlibspotify/JSession.cpp Core/Mutex.cpp
LOCAL_SHARED_LIBRARIES := libspotify-plus-plus libspotify

include $(BUILD_SHARED_LIBRARY)
