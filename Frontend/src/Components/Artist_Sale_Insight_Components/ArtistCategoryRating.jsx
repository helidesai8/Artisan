import { AgChartsReact } from "ag-charts-react";

function ArtistCategoryRating(data) {
    
        var ratingData = {
            title: {
                text: "Average rating per category",
            },
            data: data.data,
            series: [{ type: "bar", xKey: "name", yKey: "rating", xName: "Category name", yName: "Avg rating"}],
            axes: [
                {
                    type: 'number',
                    position: 'left',
                    tick:{
                        values: [0,1,2,3,4,5]
                    }
                },
                {
                    type: 'category',
                    position: 'bottom',
                }
            ],
            background: {
                fill: 'rgb(246, 246, 246)',
            },
            overlays: {
                noData: {
                  text: "No Sales recorded Yet",
                },
              },    
        };
    return(
        <div>
                <AgChartsReact options={ratingData} />
        </div>
    )
};

export default ArtistCategoryRating;