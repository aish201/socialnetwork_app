import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

interface Complaint {
  id: number;
  subject: string;
  category: string;
  priority: string;
  description: string;
  attachmentUrl: string;
  status: string;
  createdAt: string;
  username: string;
  email: string;
}

@Component({
  selector: 'app-admin-complaints',
  templateUrl: './admin-complaints.component.html',
  styleUrls: ['./admin-complaints.component.css']
})
export class AdminComplaintsComponent implements OnInit {

  complaints: Complaint[] = [];
  apiUrl = 'http://localhost:8080/api/complaints';  // ✅ FIXED URL

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadComplaints();
  }

  loadComplaints(): void {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    this.http.get<Complaint[]>(this.apiUrl, { headers }).subscribe({
      next: (data) => {
        console.log('Fetched complaints:', data);
        this.complaints = data;
      },
      error: (err) => {
        console.error('Error fetching complaints:', err);
      }
    });
  }

  updateStatus(complaintId: number, newStatus: string): void {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    this.http.put(
      `http://localhost:8080/api/complaints/${complaintId}/status`,
      { status: newStatus },
      { headers }
    ).subscribe({
      next: (res) => {
        console.log('Status updated:', res);
        this.loadComplaints(); // refresh list
      },
      error: (err) => {
        console.error('Error updating complaint status:', err);
      }
    });
  }

  deleteComplaint(id: number): void {
    if (confirm('Are you sure you want to delete this complaint?')) {
      const token = localStorage.getItem('token');
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });

      this.http.delete(`${this.apiUrl}/${id}`, { headers, responseType: 'text' }).subscribe({
        next: () => {
          alert('Complaint deleted successfully.');
          this.loadComplaints();
        },
        error: (err) => {
          console.error('Error deleting complaint:', err);
          alert('Failed to delete complaint.');
        }
      });
    }
  }
}
