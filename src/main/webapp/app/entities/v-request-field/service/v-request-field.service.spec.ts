import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVRequestField } from '../v-request-field.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../v-request-field.test-samples';

import { VRequestFieldService } from './v-request-field.service';

const requireRestSample: IVRequestField = {
  ...sampleWithRequiredData,
};

describe('VRequestField Service', () => {
  let service: VRequestFieldService;
  let httpMock: HttpTestingController;
  let expectedResult: IVRequestField | IVRequestField[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VRequestFieldService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a VRequestField', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const vRequestField = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vRequestField).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VRequestField', () => {
      const vRequestField = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vRequestField).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VRequestField', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VRequestField', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VRequestField', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVRequestFieldToCollectionIfMissing', () => {
      it('should add a VRequestField to an empty array', () => {
        const vRequestField: IVRequestField = sampleWithRequiredData;
        expectedResult = service.addVRequestFieldToCollectionIfMissing([], vRequestField);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vRequestField);
      });

      it('should not add a VRequestField to an array that contains it', () => {
        const vRequestField: IVRequestField = sampleWithRequiredData;
        const vRequestFieldCollection: IVRequestField[] = [
          {
            ...vRequestField,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVRequestFieldToCollectionIfMissing(vRequestFieldCollection, vRequestField);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VRequestField to an array that doesn't contain it", () => {
        const vRequestField: IVRequestField = sampleWithRequiredData;
        const vRequestFieldCollection: IVRequestField[] = [sampleWithPartialData];
        expectedResult = service.addVRequestFieldToCollectionIfMissing(vRequestFieldCollection, vRequestField);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vRequestField);
      });

      it('should add only unique VRequestField to an array', () => {
        const vRequestFieldArray: IVRequestField[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vRequestFieldCollection: IVRequestField[] = [sampleWithRequiredData];
        expectedResult = service.addVRequestFieldToCollectionIfMissing(vRequestFieldCollection, ...vRequestFieldArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vRequestField: IVRequestField = sampleWithRequiredData;
        const vRequestField2: IVRequestField = sampleWithPartialData;
        expectedResult = service.addVRequestFieldToCollectionIfMissing([], vRequestField, vRequestField2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vRequestField);
        expect(expectedResult).toContain(vRequestField2);
      });

      it('should accept null and undefined values', () => {
        const vRequestField: IVRequestField = sampleWithRequiredData;
        expectedResult = service.addVRequestFieldToCollectionIfMissing([], null, vRequestField, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vRequestField);
      });

      it('should return initial array if no VRequestField is added', () => {
        const vRequestFieldCollection: IVRequestField[] = [sampleWithRequiredData];
        expectedResult = service.addVRequestFieldToCollectionIfMissing(vRequestFieldCollection, undefined, null);
        expect(expectedResult).toEqual(vRequestFieldCollection);
      });
    });

    describe('compareVRequestField', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVRequestField(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVRequestField(entity1, entity2);
        const compareResult2 = service.compareVRequestField(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVRequestField(entity1, entity2);
        const compareResult2 = service.compareVRequestField(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVRequestField(entity1, entity2);
        const compareResult2 = service.compareVRequestField(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
