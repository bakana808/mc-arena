package com.octopod.mgframe;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MGFrame {

    public static List<String> getAllWorlds() {
        return Arrays.asList(MGFramePlugin.getWorldFolder().list(
                new FilenameFilter()
                {
                    @Override
                    public boolean accept(File current, String name)
                    {
                        return new File(current, name).isDirectory();
                    }
                }
        ));
    }

}
