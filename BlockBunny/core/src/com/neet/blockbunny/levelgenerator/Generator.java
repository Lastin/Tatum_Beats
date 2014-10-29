import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMap.MapLayers; //not sure
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.StaticTiledMapTile; //not sure
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import java.Math.sin;
class Generator {
  private TiledMap map;
  //Jump data
  private int cell_side = 32;
  private int lastcell_x = 0;
  private int lastcell_y = 0;
  private int jump_limit = 2;
  private int progress_counter = 0;
  private double gravity = 40//9.8;
  //
  private double speed = 48.0;
  private double jump_degree = 45.0;

  public Generator (){
    map = new TiledMap();
    MapLayers layers = map.getLayers();
    layers.add(tiledMapTileLayer);
    //tiledMapTileLayer();
  }
  private tiledMapTileLayer (int x, int y, int tile_x, int tile_y){
    lastcell_y = y/2;
    TileMapTileLayer l = new TiledMapTileLayer(x, y, cell_size, cell_size);
    ArrayList<Cell> cells = new ArrayList<Cell>();
    for(int i=0; i<cells.size(); i++){
      cells.get(i) = new Cell();
      cells.get(i).setTile(new StaticTiledMapTile(texture_region));
    }
    for(int i=0; i<x; i++){
      Random rand = new Random();
      int r = rand.nextInt(cells.size()+1)+1;
      nextCell(l, r, cells.get(r-1));
    }
  }
  private void nextCell(TiledMapTileLayer l, int r, Cell c){
    progress_counter +=32
    if(r == 0 && isWithinRange())
      return;

    l.setCell(x,yc)
  }
  private boolean isWithinRange(){
    //range: [speed^2 * sin(2degree)]/grav
    int range = (speed^2 * Math.sin(2*jump_degree))/gravity;
    //this is not correct.

  }
}

/***********
TODO:
 * Find a way to find all x and y that are in range of jump
 *
***********/
