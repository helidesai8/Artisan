import { atom } from 'recoil';

export const artistProfileState = atom({
  key: 'artistProfileState',
  default: {
    artistId: null,
    firstName: null,
    lastName: null,
    email: null,
    password: null,
    aboutMe: null,
    city: null,
    story: null,
    profileImageUrl: null,
    role: null,
  },
});