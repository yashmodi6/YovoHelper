import yt_dlp
import json

def get_video_info(url):
    """Extract all relevant video information from YouTube using yt-dlp."""
    ydl_opts = {'quiet': True}
    video_info = {}

    with yt_dlp.YoutubeDL(ydl_opts) as ydl:
        try:
            info = ydl.extract_info(url, download=False)
            # Build a JSON-compatible dictionary with relevant fields
            video_info = {
                "title": info.get("title", ""),
                "uploader": info.get("uploader", ""),
                "duration": info.get("duration", 0),  # in seconds
                "view_count": info.get("view_count", 0),
                "like_count": info.get("like_count", 0),
                "thumbnail": info.get("thumbnail", ""),
                "upload_date": info.get("upload_date", ""),
            }
        except Exception as e:
            print(f"Error extracting video info from {url}: {e}")
            video_info = {
                "error": f"Could not retrieve information: {str(e)}"
            }

    # Convert the dictionary to a JSON string and return it
    return json.dumps(video_info)

def get_download_url(url):
    """Extract the best download URL for the video."""
    ydl_opts = {'format': 'best', 'quiet': True}

    with yt_dlp.YoutubeDL(ydl_opts) as ydl:
        try:
            info_dict = ydl.extract_info(url, download=False)
            return info_dict.get("url", "")
        except Exception as e:
            print(f"Error extracting download URL from {url}: {e}")
            return ""
