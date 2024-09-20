import { atom } from 'recoil';

export const userProfileState = atom({
  key: 'userProfileState',
  default: {
    userId: null,
    firstName: null,
    lastName: null,
    email: null,
    password: null,
    contactnumber: null,
    address: null,
    profileImageUrl: null,
    role: null,
  },
});