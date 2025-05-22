import { Directive, ContentChildren } from '@angular/core';
import { ComplexBase, ArrayBase, setValue } from '@syncfusion/ej2-angular-base';
import * as i0 from "@angular/core";
let input = ['customWidth', 'format', 'hidden', 'index', 'isLocked', 'isReadOnly', 'validation', 'width'];
let outputs = [];
/**
 * `e-column` directive represent a column of the Angular Spreadsheet.
 * It must be contained in a `e-sheet` directive.
 * ```html
 * <ejs-spreadsheet>
 *   <e-sheets>
 *    <e-sheet>
 *    <e-columns>
 *    <e-column width='100'></e-column>
 *    </e-columns>
 *    </e-sheet>
 *   </e-sheets>
 * </ejs-spreadsheet>
 * ```
 */
export class ColumnDirective extends ComplexBase {
    constructor(viewContainerRef) {
        super();
        this.viewContainerRef = viewContainerRef;
        setValue('currentInstance', this, this.viewContainerRef);
        this.registerEvents(outputs);
        this.directivePropList = input;
    }
}
ColumnDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: ColumnDirective, deps: [{ token: i0.ViewContainerRef }], target: i0.ɵɵFactoryTarget.Directive });
ColumnDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "13.0.3", type: ColumnDirective, selector: "e-columns>e-column", inputs: { customWidth: "customWidth", format: "format", hidden: "hidden", index: "index", isLocked: "isLocked", isReadOnly: "isReadOnly", validation: "validation", width: "width" }, usesInheritance: true, ngImport: i0 });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: ColumnDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'e-columns>e-column',
                    inputs: input,
                    outputs: outputs,
                    queries: {}
                }]
        }], ctorParameters: function () { return [{ type: i0.ViewContainerRef }]; } });
/**
 * Column Array Directive
 * @private
 */
export class ColumnsDirective extends ArrayBase {
    constructor() {
        super('columns');
    }
}
ColumnsDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: ColumnsDirective, deps: [], target: i0.ɵɵFactoryTarget.Directive });
ColumnsDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "13.0.3", type: ColumnsDirective, selector: "e-sheet>e-columns", queries: [{ propertyName: "children", predicate: ColumnDirective }], usesInheritance: true, ngImport: i0 });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: ColumnsDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'e-sheet>e-columns',
                    queries: {
                        children: new ContentChildren(ColumnDirective)
                    },
                }]
        }], ctorParameters: function () { return []; } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY29sdW1ucy5kaXJlY3RpdmUuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi8uLi9zcmMvc3ByZWFkc2hlZXQvY29sdW1ucy5kaXJlY3RpdmUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUEsT0FBTyxFQUFFLFNBQVMsRUFBb0IsZUFBZSxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQzdFLE9BQU8sRUFBRSxXQUFXLEVBQUUsU0FBUyxFQUFFLFFBQVEsRUFBRSxNQUFNLDhCQUE4QixDQUFDOztBQUloRixJQUFJLEtBQUssR0FBYSxDQUFDLGFBQWEsRUFBRSxRQUFRLEVBQUUsUUFBUSxFQUFFLE9BQU8sRUFBRSxVQUFVLEVBQUUsWUFBWSxFQUFFLFlBQVksRUFBRSxPQUFPLENBQUMsQ0FBQztBQUNwSCxJQUFJLE9BQU8sR0FBYSxFQUFFLENBQUM7QUFDM0I7Ozs7Ozs7Ozs7Ozs7O0dBY0c7QUFTSCxNQUFNLE9BQU8sZUFBZ0IsU0FBUSxXQUE0QjtJQWdEN0QsWUFBb0IsZ0JBQWlDO1FBQ2pELEtBQUssRUFBRSxDQUFDO1FBRFEscUJBQWdCLEdBQWhCLGdCQUFnQixDQUFpQjtRQUVqRCxRQUFRLENBQUMsaUJBQWlCLEVBQUUsSUFBSSxFQUFFLElBQUksQ0FBQyxnQkFBZ0IsQ0FBQyxDQUFDO1FBQ3pELElBQUksQ0FBQyxjQUFjLENBQUMsT0FBTyxDQUFDLENBQUM7UUFDN0IsSUFBSSxDQUFDLGlCQUFpQixHQUFHLEtBQUssQ0FBQztJQUNuQyxDQUFDOzs0R0FyRFEsZUFBZTtnR0FBZixlQUFlOzJGQUFmLGVBQWU7a0JBUjNCLFNBQVM7bUJBQUM7b0JBQ1AsUUFBUSxFQUFFLG9CQUFvQjtvQkFDOUIsTUFBTSxFQUFFLEtBQUs7b0JBQ2IsT0FBTyxFQUFFLE9BQU87b0JBQ2hCLE9BQU8sRUFBRSxFQUVSO2lCQUNKOztBQXlERDs7O0dBR0c7QUFPSCxNQUFNLE9BQU8sZ0JBQWlCLFNBQVEsU0FBMkI7SUFDN0Q7UUFDSSxLQUFLLENBQUMsU0FBUyxDQUFDLENBQUM7SUFDckIsQ0FBQzs7NkdBSFEsZ0JBQWdCO2lHQUFoQixnQkFBZ0Isa0ZBSFMsZUFBZTsyRkFHeEMsZ0JBQWdCO2tCQU41QixTQUFTO21CQUFDO29CQUNQLFFBQVEsRUFBRSxtQkFBbUI7b0JBQzdCLE9BQU8sRUFBRTt3QkFDTCxRQUFRLEVBQUUsSUFBSSxlQUFlLENBQUMsZUFBZSxDQUFDO3FCQUNqRDtpQkFDSiIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IERpcmVjdGl2ZSwgVmlld0NvbnRhaW5lclJlZiwgQ29udGVudENoaWxkcmVuIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBDb21wbGV4QmFzZSwgQXJyYXlCYXNlLCBzZXRWYWx1ZSB9IGZyb20gJ0BzeW5jZnVzaW9uL2VqMi1hbmd1bGFyLWJhc2UnO1xuXG5cblxubGV0IGlucHV0OiBzdHJpbmdbXSA9IFsnY3VzdG9tV2lkdGgnLCAnZm9ybWF0JywgJ2hpZGRlbicsICdpbmRleCcsICdpc0xvY2tlZCcsICdpc1JlYWRPbmx5JywgJ3ZhbGlkYXRpb24nLCAnd2lkdGgnXTtcbmxldCBvdXRwdXRzOiBzdHJpbmdbXSA9IFtdO1xuLyoqXG4gKiBgZS1jb2x1bW5gIGRpcmVjdGl2ZSByZXByZXNlbnQgYSBjb2x1bW4gb2YgdGhlIEFuZ3VsYXIgU3ByZWFkc2hlZXQuXG4gKiBJdCBtdXN0IGJlIGNvbnRhaW5lZCBpbiBhIGBlLXNoZWV0YCBkaXJlY3RpdmUuXG4gKiBgYGBodG1sXG4gKiA8ZWpzLXNwcmVhZHNoZWV0PlxuICogICA8ZS1zaGVldHM+XG4gKiAgICA8ZS1zaGVldD5cbiAqICAgIDxlLWNvbHVtbnM+XG4gKiAgICA8ZS1jb2x1bW4gd2lkdGg9JzEwMCc+PC9lLWNvbHVtbj5cbiAqICAgIDwvZS1jb2x1bW5zPlxuICogICAgPC9lLXNoZWV0PlxuICogICA8L2Utc2hlZXRzPlxuICogPC9lanMtc3ByZWFkc2hlZXQ+XG4gKiBgYGBcbiAqL1xuQERpcmVjdGl2ZSh7XG4gICAgc2VsZWN0b3I6ICdlLWNvbHVtbnM+ZS1jb2x1bW4nLFxuICAgIGlucHV0czogaW5wdXQsXG4gICAgb3V0cHV0czogb3V0cHV0cywgICAgXG4gICAgcXVlcmllczoge1xuXG4gICAgfVxufSlcbmV4cG9ydCBjbGFzcyBDb2x1bW5EaXJlY3RpdmUgZXh0ZW5kcyBDb21wbGV4QmFzZTxDb2x1bW5EaXJlY3RpdmU+IHtcbiAgICBwdWJsaWMgZGlyZWN0aXZlUHJvcExpc3Q6IGFueTtcblx0XG5cblxuICAgIC8qKiBcbiAgICAgKiBzcGVjaWZpZXMgY3VzdG9tIHdpZHRoIG9mIHRoZSBjb2x1bW4uXG4gICAgICogQGRlZmF1bHQgZmFsc2VcbiAgICAgKi9cbiAgICBwdWJsaWMgY3VzdG9tV2lkdGg6IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIGZvcm1hdCBvZiB0aGUgY29sdW1uLlxuICAgICAqIEBkZWZhdWx0IHt9XG4gICAgICovXG4gICAgcHVibGljIGZvcm1hdDogYW55O1xuICAgIC8qKiBcbiAgICAgKiBUbyBoaWRlL3Nob3cgdGhlIGNvbHVtbiBpbiBzcHJlYWRzaGVldC5cbiAgICAgKiBAZGVmYXVsdCBmYWxzZVxuICAgICAqL1xuICAgIHB1YmxpYyBoaWRkZW46IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIGluZGV4IG9mIHRoZSBjb2x1bW4uIEJhc2VkIG9uIHRoZSBpbmRleCwgY29sdW1uIHByb3BlcnRpZXMgYXJlIGFwcGxpZWQuXG4gICAgICogQGRlZmF1bHQgMFxuICAgICAqIEBhc3B0eXBlIGludFxuICAgICAqL1xuICAgIHB1YmxpYyBpbmRleDogYW55O1xuICAgIC8qKiBcbiAgICAgKiBUbyBsb2NrL3VubG9jayB0aGUgY29sdW1uIGluIHRoZSBwcm90ZWN0ZWQgc2hlZXQuXG4gICAgICogQGRlZmF1bHQgdHJ1ZVxuICAgICAqL1xuICAgIHB1YmxpYyBpc0xvY2tlZDogYW55O1xuICAgIC8qKiBcbiAgICAgKiBSZXByZXNlbnRzIHdoZXRoZXIgYSBjb2x1bW4gaW4gdGhlIHNoZWV0IGlzIHJlYWQtb25seSBvciBub3QuIElmIHNldCB0byB0cnVlLCBpdCBwcmV2ZW50cyBlZGl0aW5nIHRoZSBzcGVjaWZpZWQgY2VsbCBpbiB0aGUgc2hlZXQuXG4gICAgICogQGRlZmF1bHQgZmFsc2VcbiAgICAgKi9cbiAgICBwdWJsaWMgaXNSZWFkT25seTogYW55O1xuICAgIC8qKiBcbiAgICAgKiBTcGVjaWZpZXMgdGhlIHZhbGlkYXRpb24gb2YgdGhlIGNvbHVtbi5cbiAgICAgKiBAZGVmYXVsdCAnJ1xuICAgICAqL1xuICAgIHB1YmxpYyB2YWxpZGF0aW9uOiBhbnk7XG4gICAgLyoqIFxuICAgICAqIFNwZWNpZmllcyB3aWR0aCBvZiB0aGUgY29sdW1uLlxuICAgICAqIEBkZWZhdWx0IDY0XG4gICAgICogQGFzcHR5cGUgaW50XG4gICAgICovXG4gICAgcHVibGljIHdpZHRoOiBhbnk7XG5cbiAgICBjb25zdHJ1Y3Rvcihwcml2YXRlIHZpZXdDb250YWluZXJSZWY6Vmlld0NvbnRhaW5lclJlZikge1xuICAgICAgICBzdXBlcigpO1xuICAgICAgICBzZXRWYWx1ZSgnY3VycmVudEluc3RhbmNlJywgdGhpcywgdGhpcy52aWV3Q29udGFpbmVyUmVmKTtcbiAgICAgICAgdGhpcy5yZWdpc3RlckV2ZW50cyhvdXRwdXRzKTtcbiAgICAgICAgdGhpcy5kaXJlY3RpdmVQcm9wTGlzdCA9IGlucHV0O1xuICAgIH1cbn1cblxuLyoqXG4gKiBDb2x1bW4gQXJyYXkgRGlyZWN0aXZlXG4gKiBAcHJpdmF0ZVxuICovXG5ARGlyZWN0aXZlKHtcbiAgICBzZWxlY3RvcjogJ2Utc2hlZXQ+ZS1jb2x1bW5zJyxcbiAgICBxdWVyaWVzOiB7XG4gICAgICAgIGNoaWxkcmVuOiBuZXcgQ29udGVudENoaWxkcmVuKENvbHVtbkRpcmVjdGl2ZSlcbiAgICB9LFxufSlcbmV4cG9ydCBjbGFzcyBDb2x1bW5zRGlyZWN0aXZlIGV4dGVuZHMgQXJyYXlCYXNlPENvbHVtbnNEaXJlY3RpdmU+IHtcbiAgICBjb25zdHJ1Y3RvcigpIHtcbiAgICAgICAgc3VwZXIoJ2NvbHVtbnMnKTtcbiAgICB9XG59Il19