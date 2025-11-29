export interface Notification {
  id: number;
  senderUsername: string;
  message: string;
  postId?: number;
  timestamp: string;
  readStatus?: boolean;
  displayMessage?: string; // for frontend display
  senderProfilePicUrl?: string;
}
