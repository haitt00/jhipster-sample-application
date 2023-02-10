import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VResponseFieldDetailComponent } from './v-response-field-detail.component';

describe('VResponseField Management Detail Component', () => {
  let comp: VResponseFieldDetailComponent;
  let fixture: ComponentFixture<VResponseFieldDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VResponseFieldDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vResponseField: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VResponseFieldDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VResponseFieldDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vResponseField on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vResponseField).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
