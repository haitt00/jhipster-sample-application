import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVEndpoint } from '../v-endpoint.model';
import { VEndpointService } from '../service/v-endpoint.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './v-endpoint-delete-dialog.component.html',
})
export class VEndpointDeleteDialogComponent {
  vEndpoint?: IVEndpoint;

  constructor(protected vEndpointService: VEndpointService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vEndpointService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
