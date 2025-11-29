export interface User {
  id?: number;
  email: string;
  password: string;
  username: string;
  profilePicUrl?: string;
  role?: string;
  active?: boolean;
}
