import { User } from "./user";

export interface Comment {
  id?: number;
  text: string;
  author?: User;
  createdAt?: string;
  authorUsername?: string;
  profilePicUrl?: string;
}


