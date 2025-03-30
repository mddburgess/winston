# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- Show author's channel handle on the authors page instead of the ID. (#4)
- Group author's comments by video, and show the video card with the comments. (#4)
- Highlight the author's comments and replies. (#4)

## [1.0.0] — 2025-03-29

### Added

- Fetch channel data from YouTube and store in the database.
- Fetch video data for a channel from YouTube and store in the database.
- Fetch comments for a video from YouTube and store in the database.
- Web UI to browse stored channels, videos, and comments.
- Web UI to fetch channels, videos, and comments.
- Cache channel and video thumbnails.

[Unreleased]: https://github.com/mddburgess/winston/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/mddburgess/winston/releases/tag/v1.0.0
