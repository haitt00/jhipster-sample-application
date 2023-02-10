import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVRequestField } from '../v-request-field.model';
import { VRequestFieldService } from '../service/v-request-field.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './v-request-field-delete-dialog.component.html',
})
export class VRequestFieldDeleteDialogComponent {
  vRequestField?: IVRequestField;

  constructor(protected vRequestFieldService: VRequestFieldService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vRequestFieldService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
