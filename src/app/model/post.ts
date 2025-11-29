import { Comment } from './comment';
import { User } from './user';

export interface Post {
  showEmojiPicker?: boolean;
  id?: number;
   content?: string;
  imageUrl?: string;
  createdBy?: string;
  profilePicUrl?: string;  
  username?: string; 
  createdAt?: string;
  likes?: number;
  liked?: boolean;
  newComment?: string;
  comments?: Comment[];
  user?:User;
  likedByCurrentUser?: boolean; 
}




