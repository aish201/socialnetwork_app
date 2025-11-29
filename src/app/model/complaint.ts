export interface Complaint {
  id?: number;
  subject: string;
  category: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
  description: string;
  attachmentUrl?: string;
  status?: 'PENDING' | 'RESOLVED' | 'REJECTED';
  createdAt?: string;
  updatedAt?: string;
  showFull?: boolean;
  username?: string;
  email?: string;
  user?: {
    username?: string;
    email?: string;
  };
}

