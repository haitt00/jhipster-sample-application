import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVResponseField } from '../v-response-field.model';
import { VResponseFieldService } from '../service/v-response-field.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './v-response-field-delete-dialog.component.html',
})
export class VResponseFieldDeleteDialogComponent {
  vResponseField?: IVResponseField;

  constructor(protected vResponseFieldService: VResponseFieldService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vResponseFieldService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
