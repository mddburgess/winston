const routes = {
  home: `/`,
  authors: {
    list: `/authors`,
    details: (authorHandle?: string) => `/authors/${authorHandle ?? `:authorHandle`}`,
  },
  channels: {
    list: `/`,
    details: (handle?: string) => `/channels/${handle ?? `:handle`}`,
  },
  settings: `/settings`,
  videos: {
    details: (videoId?: string) => `/videos/${videoId ?? `:videoId`}`,
  },
};

export { routes };
