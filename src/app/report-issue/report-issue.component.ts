import { Component } from '@angular/core';
import { ComplaintService } from 'src/app/Services/complaint.service';
import { Complaint } from 'src/app/model/complaint';

@Component({
  selector: 'app-report-issue',
  templateUrl: './report-issue.component.html'
})
export class ReportIssueComponent {
  form: Complaint = {
    subject: '',
    category: 'Bug',
    priority: 'LOW',
    description: ''
  };

  file?: File;
  fileName = '';
  loading = false;

  constructor(private complaintService: ComplaintService) {}

  onFile(e: any) {
    const f = e.target.files?.[0];
    if (f) { this.file = f; this.fileName = f.name; }
  }

  reset() {
    this.form = { subject: '', category: 'Bug', priority: 'LOW', description: '' };
    this.file = undefined;
    this.fileName = '';
  }

  submit() {
    this.loading = true;

    const submitPayload = (attachmentUrl?: string) => {
      const payload: Complaint = { ...this.form, attachmentUrl };
      this.complaintService.submitComplaint(payload).subscribe({
        next: () => {
          this.loading = false;
          alert('✅ Complaint submitted');
          this.reset();
        },
        error: (err) => {
          this.loading = false;
          console.error(err);
          alert('❌ Failed to submit complaint');
        }
      });
    };

    if (this.file) {
      this.complaintService.uploadAttachment(this.file).subscribe({
        next: res => submitPayload(res.url),
        error: () => { this.loading = false; alert('❌ File upload failed'); }
      });
    } else {
      submitPayload();
    }
  }
}


