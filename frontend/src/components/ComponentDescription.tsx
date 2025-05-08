import "../App.css";

const ComponentDescription = () => (
  <div className="page-container">
    <h1>Component Descriptions</h1>
    <p>Learn about the purpose of each PC component below:</p>
    <div className="component-description">
      <h3>CPU (Central Processing Unit)</h3>
      <p>
        The CPU is the brain of the computer. It performs all the calculations
        and processes instructions to run programs. A faster CPU improves
        overall system performance, especially for tasks like gaming, video
        editing, and multitasking.
      </p>

      <h3>GPU (Graphics Processing Unit)</h3>
      <p>
        The GPU is responsible for rendering images, videos, and animations. It
        is essential for gaming, 3D modeling, and video editing. A powerful GPU
        ensures smooth graphics performance and higher frame rates in games.
      </p>

      <h3>RAM (Random Access Memory)</h3>
      <p>
        RAM is the short-term memory of your computer. It temporarily stores
        data that the CPU needs to access quickly. More RAM allows for better
        multitasking and smoother performance in memory-intensive applications.
      </p>

      <h3>Storage (HDD/SSD)</h3>
      <p>
        Storage is where all your files, programs, and operating system are
        stored. SSDs (Solid State Drives) are faster and more reliable than HDDs
        (Hard Disk Drives), leading to quicker boot times and faster file
        access.
      </p>

      <h3>Motherboard</h3>
      <p>
        The motherboard is the main circuit board that connects all the
        components of your PC. It determines compatibility between the CPU, RAM,
        GPU, and other components.
      </p>

      <h3>PSU (Power Supply Unit)</h3>
      <p>
        The PSU provides power to all the components in your PC. A reliable PSU
        ensures stable performance and protects your components from power
        surges.
      </p>

      <h3>Case</h3>
      <p>
        The case houses all the components of your PC. It also provides airflow
        and cooling to keep your system running efficiently. Cases come in
        various sizes and designs to suit your preferences.
      </p>
    </div>
  </div>
);

export default ComponentDescription;
