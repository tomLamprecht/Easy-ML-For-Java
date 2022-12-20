package de.fhws.easyml.ai.convolutionalneuralnetwork.layer.convolutionlayer;

import de.fhws.easyml.ai.convolutionalneuralnetwork.layer.Layer;
import de.fhws.easyml.ai.convolutionalneuralnetwork.layer.OutputSizeInformation;
import de.fhws.easyml.ai.neuralnetwork.ActivationFunction;
import de.fhws.easyml.linearalgebra.Matrix;
import de.fhws.easyml.linearalgebra.Randomizer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ConvolutionLayer implements Layer {

    private final List<Filter> filters;
    private final ActivationFunction activationFunction;

    private ConvolutionLayer( List<Filter> filters, ActivationFunction activationFunction ) {
        validateFilterList( filters );

        this.filters = filters;
        this.activationFunction = activationFunction;
    }

    private void validateFilterList( List<Filter> filters ) {
        validateListIsEmpty( filters );

        validateAllFiltersSameSize( filters );
    }

    private void validateAllFiltersSameSize( List<Filter> filters ) {
        if ( !checkIfAllFiltersSameSize( filters ) )
            throw new IllegalArgumentException( "All Filters must be the same size" );
    }

    private boolean checkIfAllFiltersSameSize( List<Filter> filters ) {
        int firstFilterRows = filters.get( 0 ).getFeature().getNumRows();
        int firstFilterCols = filters.get( 0 ).getFeature().getNumCols();
        return filters.stream()
                .map( Filter::getFeature )
                .map( m -> new int[]{ m.getNumRows(), m.getNumCols() } )
                .allMatch( size -> size[0] == firstFilterRows && size[1] == firstFilterCols );
    }

    private void validateListIsEmpty( List<Filter> filters ) {
        if ( filters.isEmpty() )
            throw new IllegalArgumentException( "Filters must not be empty" );
    }

    @Override
    public List<Matrix> process( List<Matrix> input ) {
        return input.stream()
                .flatMap( m -> filters.stream().map( f -> f.doFiltering( m ) ) )
                .map( matrix -> matrix.applyAndReturn( activationFunction ) )
                .collect( Collectors.toList() );
    }

    @Override
    public OutputSizeInformation outputSizeInformation( OutputSizeInformation prevLayer ) {
        int outputRowSize = filters.get( 0 ).calculateResultDataRows( prevLayer.getRows() );
        int outputColSize = filters.get( 0 ).calculateResultDataCols( prevLayer.getCols() );
        int outputAmountOfChannel = prevLayer.getChannels() * filters.size();
        return new OutputSizeInformation( outputRowSize, outputColSize, outputAmountOfChannel);
    }


    public static class Builder {
        public static final Randomizer DEFAULT_RANDOMIZER = new Randomizer( -1, 1 );
        public static final int[] DEFAULT_STRIDING = new int[]{ 1, 1 };
        public static final boolean DEFAULT_PADDING_STATE = false;
        public static final ActivationFunction RELU = ( x ) -> x < 0 ? 0 : x;

        private final int filterRows, filterCols;
        private final int amountOfFilter;

        private Randomizer randomizer = DEFAULT_RANDOMIZER;
        private final int[] striding = DEFAULT_STRIDING;
        private boolean padding = DEFAULT_PADDING_STATE;
        private ActivationFunction activationFunction = RELU;

        public Builder( int filterSize, int amountOfFilter ) {
            this( filterSize, filterSize, amountOfFilter );
        }

        public Builder( int filterRows, int filterCols, int amountOfFilter ) {
            this.filterRows = filterRows;
            this.filterCols = filterCols;
            this.amountOfFilter = amountOfFilter;
        }

        public Builder withRandomizer( Randomizer randomizer ) {
            this.randomizer = randomizer;
            return this;
        }


        public Builder withPadding( boolean padding ) {
            this.padding = padding;
            return this;
        }

        public Builder withStriding( int row, int col ) {
            striding[0] = row;
            striding[1] = col;
            return this;
        }

        public Builder withActivationFunction( ActivationFunction activationFunction ) {
            this.activationFunction = activationFunction;
            return this;
        }

        public ConvolutionLayer build() {
            List<Filter> filters = Stream
                    .generate( () -> Filter.createRandomizedFilter( filterRows, filterCols, randomizer, striding[0], striding[1], padding ) )
                    .limit( amountOfFilter )
                    .collect( Collectors.toList() );

            return new ConvolutionLayer( filters, activationFunction );
        }


    }
}
