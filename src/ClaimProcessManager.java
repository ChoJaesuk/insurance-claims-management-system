package src;

import java.util.*;
public interface ClaimProcessManager {

    void add();
    void update();
    void delete();
    Claim getOne();
    List<Claim> getAll();
    void getMenu();

}
