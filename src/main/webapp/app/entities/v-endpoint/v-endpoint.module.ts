import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VEndpointComponent } from './list/v-endpoint.component';
import { VEndpointDetailComponent } from './detail/v-endpoint-detail.component';
import { VEndpointUpdateComponent } from './update/v-endpoint-update.component';
import { VEndpointDeleteDialogComponent } from './delete/v-endpoint-delete-dialog.component';
import { VEndpointRoutingModule } from './route/v-endpoint-routing.module';

@NgModule({
  imports: [SharedModule, VEndpointRoutingModule],
  declarations: [VEndpointComponent, VEndpointDetailComponent, VEndpointUpdateComponent, VEndpointDeleteDialogComponent],
})
export class VEndpointModule {}
