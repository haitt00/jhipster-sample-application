import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VRequestFieldComponent } from './list/v-request-field.component';
import { VRequestFieldDetailComponent } from './detail/v-request-field-detail.component';
import { VRequestFieldUpdateComponent } from './update/v-request-field-update.component';
import { VRequestFieldDeleteDialogComponent } from './delete/v-request-field-delete-dialog.component';
import { VRequestFieldRoutingModule } from './route/v-request-field-routing.module';

@NgModule({
  imports: [SharedModule, VRequestFieldRoutingModule],
  declarations: [VRequestFieldComponent, VRequestFieldDetailComponent, VRequestFieldUpdateComponent, VRequestFieldDeleteDialogComponent],
})
export class VRequestFieldModule {}
