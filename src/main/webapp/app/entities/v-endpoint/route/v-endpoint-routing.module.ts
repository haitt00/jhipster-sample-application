import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VEndpointComponent } from '../list/v-endpoint.component';
import { VEndpointDetailComponent } from '../detail/v-endpoint-detail.component';
import { VEndpointUpdateComponent } from '../update/v-endpoint-update.component';
import { VEndpointRoutingResolveService } from './v-endpoint-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vEndpointRoute: Routes = [
  {
    path: '',
    component: VEndpointComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VEndpointDetailComponent,
    resolve: {
      vEndpoint: VEndpointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VEndpointUpdateComponent,
    resolve: {
      vEndpoint: VEndpointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VEndpointUpdateComponent,
    resolve: {
      vEndpoint: VEndpointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vEndpointRoute)],
  exports: [RouterModule],
})
export class VEndpointRoutingModule {}
